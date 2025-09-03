package finalmission.domain.gym;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import finalmission.domain.member.Address;
import finalmission.exception.BusinessRuleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GymTest {

    @Test
    @DisplayName("헬스장을 생성한다.")
    void newGym() {
        var gym = new Gym("짐박스", new Address("군자로 123-1", "지하 1층"));
        assertThat(gym).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("헬스장 이름은 3자 이상 10자 이하여야 한다.")
    @ValueSource(strings = {"", "헬", "헬스", "헬스헬스헬스헬스헬스헬"})
    void validateName(String name) {
        assertThatThrownBy(() -> new Gym(name, new Address("군자로 123-1", "지하 1층")))
            .isInstanceOf(BusinessRuleException.class);
    }
}
