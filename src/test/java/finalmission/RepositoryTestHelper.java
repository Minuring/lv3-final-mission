package finalmission;

import finalmission.domain.gym.Gym;
import finalmission.domain.member.Member;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class RepositoryTestHelper {

    @Autowired
    private EntityManager em;

    public Member saveAnyMember() {
        var member = TestFixtures.anyMember();
        em.persist(member);
        return member;
    }

    public Gym saveAnyGym() {
        var gym = TestFixtures.anyGym();
        em.persist(gym);
        return gym;
    }
}
