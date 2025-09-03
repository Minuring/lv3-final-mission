package finalmission.domain.member;

import finalmission.exception.ElementNotFoundException;
import java.util.NoSuchElementException;
import org.springframework.data.repository.ListCrudRepository;

public interface MemberRepository extends ListCrudRepository<Member, String> {

    default Member getById(final String id) {
        return findById(id)
            .orElseThrow(() -> new ElementNotFoundException("존재하지 않는 사용자 ID입니다 : " + id));
    }
}
