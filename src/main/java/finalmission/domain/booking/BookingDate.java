package finalmission.domain.booking;

import finalmission.exception.BusinessRuleException;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Embeddable
public class BookingDate {

    private LocalDate date;

    private BookingDate(final LocalDate date) {
        this.date = date;
    }

    public static BookingDate of(final int year, final int month, final int dayOfMonth) {
        return BookingDate.of(LocalDate.of(year, month, dayOfMonth));
    }

    public static BookingDate of(final LocalDate date) {
        return new BookingDate(date);
    }

    public static BookingDate ofNew(final int year, final int month, final int dayOfMonth) {
        return ofNew(LocalDate.of(year, month, dayOfMonth));
    }

    public static BookingDate ofNew(final LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new BusinessRuleException("지난 날짜로 예약할 수 없습니다 : " + date);
        }
        return new BookingDate(date);
    }
}
