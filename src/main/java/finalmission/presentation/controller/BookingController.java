package finalmission.presentation.controller;

import finalmission.application.BookingService;
import finalmission.domain.AuthInfo;
import finalmission.presentation.request.BookingRequest;
import finalmission.presentation.request.ModifyBookingRequest;
import finalmission.presentation.response.BookingResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void book(final AuthInfo authInfo, @Valid @RequestBody final BookingRequest request) {
        bookingService.book(authInfo.memberId(), UUID.fromString(request.gymId()), request.date());
    }

    @PatchMapping("/{id}")
    public BookingResponse modify(@PathVariable("id") final UUID id, final AuthInfo authInfo, @Valid @RequestBody final ModifyBookingRequest request) {
        var booking = bookingService.modifyDate(id, authInfo.memberId(), request.dateToModify());
        return BookingResponse.from(booking);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable("id") final UUID id, final AuthInfo authInfo) {
        bookingService.cancel(id, authInfo.memberId());
    }

    @GetMapping("/mine")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponse> getMyBookings(final AuthInfo authInfo) {
        var memberId = authInfo.memberId();
        var myBookings = bookingService.getMyBookings(memberId);
        return BookingResponse.from(myBookings);
    }
}


