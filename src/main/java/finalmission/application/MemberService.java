package finalmission.application;

import finalmission.domain.AuthInfo;
import finalmission.domain.member.Member;
import finalmission.domain.member.MemberRepository;
import finalmission.domain.member.MemberTokenProvider;
import finalmission.exception.InvalidArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;
    private final MemberTokenProvider tokenProvider;

    public void register(final String id, final String password, final String name) {
        var member = new Member(id, password, name);
        repository.save(member);
    }

    public String login(final String id, final String password) {
        return repository.findById(id)
            .filter(m -> m.isSamePassword(password))
            .map(member -> new AuthInfo(member.getId(), member.getRole()))
            .map(tokenProvider::generateToken)
            .orElseThrow(() -> new InvalidArgumentException("아이디 또는 비밀번호가 틀렸습니다."));
    }
}
