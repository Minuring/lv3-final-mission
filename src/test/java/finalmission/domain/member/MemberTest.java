package finalmission.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import finalmission.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    @Test
    @DisplayName("사용자를 생성한다.")
    void newMember() {
        var member = new Member("popo", "password", "포포");
        assertThat(member).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("사용자 이름은 2자 이상 5자 이하여야 한다.")
    @ValueSource(strings = {"", "포", "포포포포포포"})
    void validateName(String name) {
        assertThatThrownBy(() -> new Member("popo", "password", name))
            .isInstanceOf(BusinessRuleException.class);
    }
}
