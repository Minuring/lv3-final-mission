package finalmission.domain.gym;

import finalmission.domain.member.Address;
import finalmission.exception.BusinessRuleException;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Getter
@ToString
@Entity
public class Gym {

    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 10;

    @Id
    private final UUID id = UUID.randomUUID();
    private String name;
    @Embedded
    private Address address;

    public Gym(final String name, final Address address) {
        validateNameLength(name);
        this.name = name;
        this.address = address;
    }

    private void validateNameLength(final String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            var message = String.format("헬스장 이름은 %d자 이상 %d자 이하여야 합니다.", MIN_NAME_LENGTH, MAX_NAME_LENGTH);
            throw new BusinessRuleException(message);
        }
    }
}
