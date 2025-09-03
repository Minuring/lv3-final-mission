package finalmission.application;

import finalmission.domain.member.Address;
import finalmission.domain.gym.Gym;
import finalmission.domain.gym.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GymService {

    private final GymRepository repository;

    public void register(final String name, final Address address) {
        var gym = new Gym(name, address);
        repository.save(gym);
    }
}
