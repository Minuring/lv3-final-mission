package finalmission.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import finalmission.domain.AuthInfo;
import finalmission.domain.member.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtMemberTokenProviderTest {

    private final JwtMemberTokenProvider provider = new JwtMemberTokenProvider();

    @Test
    @DisplayName("사용자 ID와 역할로 이루어진 토큰을 생성한다.")
    void generateToken() {
        var authInfo = new AuthInfo("popoId", MemberRole.USER);

        var token = provider.generateToken(authInfo);

        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("사용자 ID와 역할을 토큰으로부터 추출한다.")
    void extractAuthInfo() {
        var authInfo = new AuthInfo("popoId", MemberRole.USER);
        var token = provider.generateToken(authInfo);

        var extractedAuthInfo = provider.extractAuthInfo(token);

        assertAll(
            () -> assertThat(extractedAuthInfo.memberId()).isEqualTo("popoId"),
            () -> assertThat(extractedAuthInfo.role()).isEqualTo(MemberRole.USER)
        );
    }
}
