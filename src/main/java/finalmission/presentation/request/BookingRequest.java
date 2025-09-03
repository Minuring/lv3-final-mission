package finalmission.presentation.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record BookingRequest(
    @NotBlank
    String gymId,
    @FutureOrPresent
    LocalDate date
) {

}
