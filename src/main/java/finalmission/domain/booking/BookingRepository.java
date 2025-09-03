package finalmission.domain.booking;

import finalmission.domain.gym.Gym;
import finalmission.domain.member.Member;
import finalmission.exception.ElementNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

public interface BookingRepository extends ListCrudRepository<Booking, UUID> {

    default Booking getById(final UUID id) {
        return findById(id)
            .orElseThrow(() -> new ElementNotFoundException("존재하지 않는 예약 ID입니다 : " + id));
    }

    List<Booking> findAllByMember(Member member);

    boolean existsByMemberAndGymAndDate(Member member, Gym gym, BookingDate date);
}
