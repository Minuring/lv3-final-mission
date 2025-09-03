package finalmission.presentation.response;

import finalmission.domain.booking.Booking;
import java.util.List;

public record BookingResponse(
    String id,
    String memberId,
    String memberName,
    String gymId,
    String gymName,
    String gymAddress,
    String date
    ) {

    public static BookingResponse from(final Booking booking) {
        return new BookingResponse(
            booking.getId().toString(),
            booking.getMember().getId(),
            booking.getMember().getName(),
            booking.getGym().getId().toString(),
            booking.getGym().getName(),
            booking.getGym().getAddress().street() + " " + booking.getGym().getAddress().detail(),
            booking.getDate().toString()
        );
    }

    public static List<BookingResponse> from(final List<Booking> bookings) {
        return bookings.stream()
            .map(BookingResponse::from)
            .toList();
    }
}
