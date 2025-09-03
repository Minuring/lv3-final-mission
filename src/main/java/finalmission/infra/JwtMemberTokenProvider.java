package finalmission.infra;

import finalmission.domain.AuthInfo;
import finalmission.domain.AuthenticationException;
import finalmission.domain.member.MemberRole;
import finalmission.domain.member.MemberTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtMemberTokenProvider implements MemberTokenProvider {

    private static final String MEMBER_ROLE = "role";
    private final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    public String generateToken(final AuthInfo authInfo) {
        return Jwts.builder()
            .subject(authInfo.memberId())
            .claim(MEMBER_ROLE, authInfo.role())
            .signWith(SECRET_KEY)
            .compact();
    }

    public AuthInfo extractAuthInfo(final String token) {
        try {
            var payload = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            var memberId = payload.getSubject();
            var memberRole = MemberRole.valueOf(payload.get(MEMBER_ROLE, String.class));
            return new AuthInfo(memberId, memberRole);
        } catch (JwtException e) {
            throw new AuthenticationException("유효하지 않은 토큰입니다.");
        }
    }
}
