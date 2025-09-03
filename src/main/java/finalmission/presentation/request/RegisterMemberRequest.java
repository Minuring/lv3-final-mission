package finalmission.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterMemberRequest(
    @NotBlank
    String id,
    @NotBlank
    String password,
    @NotBlank
    String name
) {

}
