package finalmission.domain.gym;

import finalmission.exception.ElementNotFoundException;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface GymRepository extends ListCrudRepository<Gym, UUID> {

    default Gym getById(final UUID id) {
        return findById(id)
            .orElseThrow(() -> new ElementNotFoundException("존재하지 않는 헬스장 ID입니다 : " + id));
    }
}
