package finalmission.presentation;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import finalmission.domain.AuthInfo;
import finalmission.domain.member.MemberTokenProvider;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class AuthInfoArgumentResolverTest {

    private static final String ENDPOINT_WITH_AUTH_INFO_PARAMETER = "/endpointWithAuthInfoParameter";
    private static final String ENDPOINT_WITHOUT_AUTH_INFO_PARAMETER = "/endpointWithoutAuthInfoParameter";

    private final MemberTokenProvider memberTokenProvider = mock(MemberTokenProvider.class);
    private final AuthInfoArgumentResolver argumentResolver = new AuthInfoArgumentResolver(memberTokenProvider);

    private final MockMvc mockMvc = MockMvcBuilders
        .standaloneSetup(new TestController())
        .setCustomArgumentResolvers(argumentResolver)
        .build();

    @Test
    @DisplayName("쿠키의 토큰으로부터 컨트롤러 메서드 파라미터의 AuthInfo 객체를 바인딩한다.")
    void resolveArgument() throws Exception {
        var tokenInCookie = "token";
        doReturn(new AuthInfo("popo")).when(memberTokenProvider).extractAuthInfo(tokenInCookie);

        mockMvc.perform(get(ENDPOINT_WITH_AUTH_INFO_PARAMETER)
            .cookie(new Cookie("token", tokenInCookie)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.memberId").value("popo"));
    }

    @Test
    @DisplayName("컨트롤러 메서드 파라미터에 AuthInfo 객체가 없으면 아무 일도 일어나지 않는다.")
    void dontResolveArgument() throws Exception {
        mockMvc.perform(get(ENDPOINT_WITHOUT_AUTH_INFO_PARAMETER))
            .andExpect(status().isOk());

        verify(memberTokenProvider, never()).extractAuthInfo(anyString());
    }

    @Controller
    private static class TestController {

        @GetMapping(ENDPOINT_WITH_AUTH_INFO_PARAMETER)
        @ResponseStatus(HttpStatus.OK)
        @ResponseBody
        public AuthInfo endpointWithAuthInfoParameter(final AuthInfo authInfo) {
            return authInfo;
        }

        @GetMapping(ENDPOINT_WITHOUT_AUTH_INFO_PARAMETER)
        @ResponseStatus(HttpStatus.OK)
        public void endpointWithoutAuthInfoParameter() { }
    }
}
