package finalmission.presentation.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ModifyBookingRequest(
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate dateToModify
) {

}
