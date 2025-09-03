package finalmission.application;

import finalmission.domain.member.Address;
import finalmission.domain.gym.GymRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GymServiceTest {

    private final GymRepository gymRepository = Mockito.mock(GymRepository.class);
    private final GymService gymService = new GymService(gymRepository);

    @Test
    @DisplayName("헬스장을 등록한다.")
    void register() {
        gymService.register("짐박스", new Address("도로명주소", "상세주소"));
        Mockito.verify(gymRepository).save(Mockito.any());
    }
}
