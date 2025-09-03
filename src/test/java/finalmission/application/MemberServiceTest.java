package finalmission.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import finalmission.domain.AuthInfo;
import finalmission.domain.member.Member;
import finalmission.domain.member.MemberRepository;
import finalmission.domain.member.MemberTokenProvider;
import finalmission.exception.InvalidArgumentException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MemberServiceTest {

    private final MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    private final MemberTokenProvider tokenProvider = Mockito.mock(MemberTokenProvider.class);
    private final MemberService memberService = new MemberService(memberRepository, tokenProvider);

    @Test
    @DisplayName("사용자를 등록한다.")
    void register() {
        memberService.register("popo", "password", "포포");
        Mockito.verify(memberRepository).save(any());
    }

    @Test
    @DisplayName("로그인을 하면 토큰을 반환한다.")
    void login() {
        Mockito
            .doReturn(Optional.of(new Member("popo", "password", "포포")))
            .when(memberRepository).findById("popo");
        Mockito
            .doReturn("token")
            .when(tokenProvider).generateToken(any(AuthInfo.class));

        var token = memberService.login("popo", "password");

        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("아이디가 존재하지 않으면 로그인에 실패한다.")
    void loginIdWrong() {
        Mockito
            .doReturn(Optional.empty())
            .when(memberRepository).findById("popo");

        assertThatThrownBy(() -> memberService.login("popo", "password"))
            .isInstanceOf(InvalidArgumentException.class);
    }

    @Test
    @DisplayName("비밀번호가 틀리면 로그인에 실패한다.")
    void loginPasswordWrong() {
        Mockito
            .doReturn(Optional.of(new Member("popo", "password", "포포")))
            .when(memberRepository).findById("popo");

        assertThatThrownBy(() -> memberService.login("popo", "xxxxxxxx"))
            .isInstanceOf(InvalidArgumentException.class);
    }
}
