package finalmission.domain.member;

import finalmission.domain.AuthInfo;

public interface MemberTokenProvider {

    String generateToken(AuthInfo authInfo);

    AuthInfo extractAuthInfo(String token);
}
