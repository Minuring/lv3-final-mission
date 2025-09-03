package finalmission.domain.booking;

import static finalmission.TestFixtures.anyBookingDate;
import static org.assertj.core.api.Assertions.assertThat;

import finalmission.domain.gym.Gym;
import finalmission.domain.member.Address;
import finalmission.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookingTest {

    @Test
    @DisplayName("예약을 생성한다.")
    void newBooking() {
        var booking = new Booking(
            new Member("popo", "password", "포포"),
            new Gym("짐박스", new Address("군자로123-1", "지하 1층")),
            BookingDate.of(2025, 6, 17)
        );
        assertThat(booking).isNotNull();
    }

    @Test
    @DisplayName("예약 날짜를 변경한다.")
    void modifyDate() {
        var booking = new Booking(
            new Member("popo", "password", "포포"),
            new Gym("짐박스", new Address("군자로123-1", "지하 1층")),
            BookingDate.of(2025, 6, 17)
        );

        var dateToModify = anyBookingDate();
        booking.modifyDate(dateToModify);

        assertThat(booking.getDate()).isEqualTo(dateToModify);
    }
}
