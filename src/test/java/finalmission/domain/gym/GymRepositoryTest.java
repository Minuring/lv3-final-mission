package finalmission.domain.gym;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import finalmission.domain.member.Address;
import finalmission.exception.ElementNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class GymRepositoryTest {

    @Autowired
    private GymRepository gymRepository;

    @Test
    @DisplayName("헬스장을 저장한다.")
    void save() {
        var gym = new Gym("짐박스", new Address("도로명주소", "상세주소"));

        var savedGym = gymRepository.save(gym);

        assertThat(gymRepository.findById(savedGym.getId())).hasValue(gym);
    }

    @Test
    @DisplayName("조회하려는 ID가 없으면 예외가 발생한다.")
    void getById() {
        assertThatThrownBy(() -> gymRepository.getById(UUID.randomUUID()))
            .isInstanceOf(ElementNotFoundException.class);
    }
}
