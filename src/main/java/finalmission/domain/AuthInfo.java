package finalmission.domain;

import finalmission.domain.member.MemberRole;

public record AuthInfo(
    String memberId,
    MemberRole role
) {

    public AuthInfo(final String memberId) {
        this(memberId, MemberRole.USER);
    }

    public boolean isAdmin() {
        return MemberRole.ADMIN == role;
    }
}
