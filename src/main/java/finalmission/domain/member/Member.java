package finalmission.domain.member;

import finalmission.exception.BusinessRuleException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Getter
@ToString
@Entity
public class Member {

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 5;

    @Id
    private String id;
    private String password;
    private String name;
    private MemberRole role;

    public Member(final String id, final String password, final String name) {
        validateNameLength(name);
        this.id = id;
        this.password = password;
        this.name = name;
        this.role = MemberRole.USER;
    }

    public boolean isSamePassword(final String password) {
        return this.password.equals(password);
    }

    private void validateNameLength(final String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            var message = String.format("사용자 이름은 %d자 이상 %d자 이하여야 합니다.", MIN_NAME_LENGTH, MAX_NAME_LENGTH);
            throw new BusinessRuleException(message);
        }
    }
}
