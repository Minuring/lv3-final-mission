package finalmission.presentation.controller;

import static finalmission.TestFixtures.anyBooking;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import finalmission.application.BookingService;
import finalmission.domain.AuthInfo;
import finalmission.domain.AuthenticationException;
import finalmission.presentation.GlobalExceptionHandler;
import finalmission.presentation.StubAuthInfoArgumentResolver;
import jakarta.servlet.http.Cookie;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class BookingControllerTest {

    private final BookingService bookingService = Mockito.mock(BookingService.class);
    private final StubAuthInfoArgumentResolver authInfoArgumentResolver = new StubAuthInfoArgumentResolver();
    private final MockMvc mockMvc = MockMvcBuilders
        .standaloneSetup(new BookingController(bookingService))
        .setControllerAdvice(new GlobalExceptionHandler())
        .setCustomArgumentResolvers(authInfoArgumentResolver)
        .build();

    @Test
    @DisplayName("예약을 하면 CREATED를 응답한다.")
    void book() throws Exception {
        authInfoArgumentResolver.stub(new AuthInfo("popo"));

        mockMvc.perform(post("/bookings")
            .cookie(new Cookie("token", "token"))
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "gymId": "3bfc19bd-23df-482e-8857-3d4d3a173482",
                    "date": "2999-12-31"
                }
                """)
        ).andExpect(status().isCreated());

        Mockito.verify(bookingService).book(
            eq("popo"),
            eq(UUID.fromString("3bfc19bd-23df-482e-8857-3d4d3a173482")),
            eq(LocalDate.of(2999, 12, 31))
        );
    }

    @Test
    @DisplayName("로그인하지 않고 예약하려 하면 UNAUTHORIZED를 응답한다.")
    void bookShouldLogin() throws Exception {
        authInfoArgumentResolver.stubNotLogin();

        mockMvc.perform(post("/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "gymId": "3bfc19bd-23df-482e-8857-3d4d3a173482",
                    "date": "2025-06-17"
                }
                """)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("내 예약을 조회하면 OK와 함께 예약들로 이루어진 컬렉션을 응답한다.")
    void getMyBookings() throws Exception {
        authInfoArgumentResolver.stub(new AuthInfo("popo"));
        Mockito.doReturn(List.of(anyBooking(), anyBooking())).when(bookingService).getMyBookings(eq("popo"));

        mockMvc.perform(get("/bookings/mine")
                .cookie(new Cookie("token", "token"))
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$..['id', 'memberId', 'gymId', 'date']").exists());
    }

    @Test
    @DisplayName("로그인하지 않고 내 예약을 조회하려 하면 UNAUTHORIZED를 응답한다.")
    void getMyBookingsShouldLogin() throws Exception {
        authInfoArgumentResolver.stubNotLogin();

        mockMvc.perform(get("/bookings/mine"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("예약 날짜를 수정하면 OK와 함께 수정된 예약을 응답한다.")
    void modifyDate() throws Exception {
        authInfoArgumentResolver.stub(new AuthInfo("popo"));
        var booking = anyBooking();
        var dateToModify = LocalDate.of(2025, 6, 17);
        Mockito.doReturn(booking).when(bookingService).modifyDate(eq(booking.getId()), any(), eq(dateToModify));

        mockMvc.perform(patch("/bookings/{id}", booking.getId())
                .cookie(new Cookie("token", "token"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "dateToModify": "2025-06-17"
                    }
                    """)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$..['id', 'memberId', 'gymId', 'date']").exists());
    }

    @Test
    @DisplayName("예약 수정 시 사용자 인증에 실패 하면 UNAUTHORIZED를 응답한다.")
    void modifyDateUnauthorized() throws Exception {
        authInfoArgumentResolver.stub(new AuthInfo("xxxx"));
        Mockito.doThrow(AuthenticationException.class).when(bookingService).modifyDate(any(), any(), any());

        mockMvc.perform(patch("/bookings/{id}", UUID.randomUUID())
            .cookie(new Cookie("token", "token"))
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "dateToModify": "2025-06-17"
                }
                """)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("예약을 취소하면 NO CONTENT를 응답한다.")
    void cancelBooking() throws Exception {
        authInfoArgumentResolver.stub(new AuthInfo("popo"));
        var booking = anyBooking();
        Mockito.doNothing().when(bookingService).cancel(eq(booking.getId()), eq("popo"));

        mockMvc.perform(delete("/bookings/{id}", booking.getId())
            .cookie(new Cookie("token", "token"))
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("예약 취소 시 사용자 인증에 실패 하면 UNAUTHORIZED를 응답한다.")
    void cancelBookingUnauthorized() throws Exception {
        authInfoArgumentResolver.stub(new AuthInfo("xxxx"));
        Mockito.doThrow(AuthenticationException.class).when(bookingService).cancel(any(), any());

        mockMvc.perform(delete("/bookings/{id}", UUID.randomUUID())
            .cookie(new Cookie("token", "token"))
        ).andExpect(status().isUnauthorized());
    }
}
