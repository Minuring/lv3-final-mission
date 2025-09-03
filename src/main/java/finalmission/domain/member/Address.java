package finalmission.domain.member;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(
    String street,
    String detail
) {

}
