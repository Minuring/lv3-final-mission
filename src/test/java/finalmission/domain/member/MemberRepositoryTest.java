package finalmission.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import finalmission.exception.ElementNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("사용자를 저장한다.")
    void save() {
        var member = new Member("popo", "password", "포포");

        var savedMember = memberRepository.save(member);

        assertThat(memberRepository.findById(savedMember.getId())).hasValue(member);
    }

    @Test
    @DisplayName("조회하려는 ID가 없으면 예외가 발생한다.")
    void getById() {
        assertThatThrownBy(() -> memberRepository.getById("abcd"))
            .isInstanceOf(ElementNotFoundException.class);
    }
}
