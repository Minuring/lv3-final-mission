package finalmission.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
@DisplayName("공휴일 API 실제 요청 테스트")
class HolidayCheckerClientTest {

    @Autowired
    private HolidayCheckerClient client;

    @Test
    @DisplayName("[true] 주어진 날짜가 공휴일인 지 알 수 있다.")
    void isHolidayTrue() {
        var result = client.isHoliday(LocalDate.of(2025, 5, 5));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("[false] 주어진 날짜가 공휴일인 지 알 수 있다.")
    void isHolidayFalse() {
        var result = client.isHoliday(LocalDate.of(2025, 5, 4));
        assertThat(result).isFalse();
    }
}
