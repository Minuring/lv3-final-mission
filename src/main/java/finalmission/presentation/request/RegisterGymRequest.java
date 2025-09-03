package finalmission.presentation.request;

import finalmission.domain.member.Address;
import jakarta.validation.constraints.NotBlank;

public record RegisterGymRequest(
    @NotBlank
    String name,
    @NotBlank
    String street,
    @NotBlank
    String detail
) {

    public Address address() {
        return new Address(street, detail);
    }
}
