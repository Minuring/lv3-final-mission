package finalmission.domain.booking;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import finalmission.exception.BusinessRuleException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookingDateTest {

    @Test
    @DisplayName("과거 날짜로 예약 날짜를 생성할 수 없다.")
    void ofNew() {
        var yesterday = LocalDate.now().minusDays(1);
        assertThatThrownBy(() -> BookingDate.ofNew(yesterday))
            .isInstanceOf(BusinessRuleException.class);
    }
}
