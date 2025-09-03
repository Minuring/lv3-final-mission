package finalmission.presentation.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import finalmission.application.GymService;
import finalmission.domain.AuthInfo;
import finalmission.domain.member.Address;
import finalmission.domain.member.MemberRole;
import finalmission.presentation.GlobalExceptionHandler;
import finalmission.presentation.StubAuthInfoArgumentResolver;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class GymControllerTest {

    private final GymService gymService = Mockito.mock(GymService.class);
    private final StubAuthInfoArgumentResolver authInfoArgumentResolver = new StubAuthInfoArgumentResolver();
    private final MockMvc mockMvc = MockMvcBuilders
        .standaloneSetup(new GymController(gymService))
        .setCustomArgumentResolvers(authInfoArgumentResolver)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();

    @Test
    @DisplayName("헬스장을 등록하면 CREATED를 응답한다.")
    void register() throws Exception {
        authInfoArgumentResolver.stub(new AuthInfo("popo", MemberRole.ADMIN));

        mockMvc.perform(post("/gyms")
                .cookie(new Cookie("token", "token"))
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "name": "짐박스",
                    "street": "군자로 123",
                    "detail": "지하 1층"
                }
                """)
        ).andExpect(status().isCreated());
        Mockito.verify(gymService).register(eq("짐박스"), eq(new Address("군자로 123", "지하 1층")));
    }

    @Test
    @DisplayName("관리자가 아닌 유저가 헬스장을 등록하려 하면 UNAUTHORIZED를 응답한다.")
    void registerCanOnlyAdmin() throws Exception {
        authInfoArgumentResolver.stub(new AuthInfo("popo", MemberRole.USER));

        mockMvc.perform(post("/gyms")
            .cookie(new Cookie("token", "token"))
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "name": "짐박스",
                    "street": "군자로 123",
                    "detail": "지하 1층"
                }
                """)
        ).andExpect(status().isUnauthorized());
        Mockito.verify(gymService, never()).register(eq("짐박스"), eq(new Address("군자로 123", "지하 1층")));
    }

    @ParameterizedTest
    @DisplayName("등록할 헬스장의 내용이 하나라도 요청 내용이 비어있으면 BAD REQUEST를 응답한다.")
    @ValueSource(strings = {
        "{ \"name\": \"\", \"street\": \"군자로 123\", \"detail\": \"지하 1층\" }",
        "{ \"name\": \"짐박스\", \"street\": \"\", \"detail\": \"지하 1층\" }",
        "{ \"name\": \"짐박스\", \"street\": \"군자로 123\", \"detail\": \"\" }",
    })
    void validateBlank(String requestJson) throws Exception {
        authInfoArgumentResolver.stub(new AuthInfo("popo", MemberRole.ADMIN));

        mockMvc.perform(post("/gyms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
        ).andExpect(status().isBadRequest());
    }
}
