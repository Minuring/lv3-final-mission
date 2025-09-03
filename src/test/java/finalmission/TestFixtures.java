package finalmission;

import finalmission.domain.member.Address;
import finalmission.domain.booking.Booking;
import finalmission.domain.booking.BookingDate;
import finalmission.domain.gym.Gym;
import finalmission.domain.member.Member;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TestFixtures {

    private static final AtomicLong SEQUENCE_GENERATOR = new AtomicLong();

    public static Member anyMember() {
        var id = "mem" + SEQUENCE_GENERATOR.incrementAndGet();
        return new Member(id, "test", "name");
    }

    public static Gym anyGym() {
        var name = "gym" + SEQUENCE_GENERATOR.incrementAndGet();
        return new Gym(name, new Address("test street", "test detail"));
    }

    public static LocalDate anyLocalDate() {
        return LocalDate.of(
            new Random().nextInt(2001, 2025),
            new Random().nextInt(1, 12),
            new Random().nextInt(1, 28)
        );
    }

    public static LocalDate anyFutureLocalDate() {
        return LocalDate.now().plusDays(new Random().nextInt(1, 999));
    }

    public static BookingDate anyBookingDate() {
        return BookingDate.of(anyLocalDate());
    }

    public static Booking anyBooking() {
        return new Booking(anyMember(), anyGym(), anyBookingDate());
    }

    private static UUID generateUUID() {
        return UUID.randomUUID();
    }
}
