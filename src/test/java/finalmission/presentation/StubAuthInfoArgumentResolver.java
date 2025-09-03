package finalmission.presentation;

import finalmission.domain.AuthInfo;
import finalmission.domain.AuthenticationException;
import finalmission.infra.JwtMemberTokenProvider;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class StubAuthInfoArgumentResolver extends AuthInfoArgumentResolver {

    private AuthInfo stub = null;
    private boolean stubbedNotLogin = false;

    public StubAuthInfoArgumentResolver() {
        super(new JwtMemberTokenProvider());
    }

    public StubAuthInfoArgumentResolver(final AuthInfo stub) {
        super(new JwtMemberTokenProvider());
        this.stub = stub;
    }

    public void stubNotLogin() {
        stubbedNotLogin = true;
    }

    public void stub(final AuthInfo stub) {
        this.stub = stub;
    }

    @Override
    public Object resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) {
        if (stubbedNotLogin) {
            throw new AuthenticationException("로그인 되지 않도록 스텁되었습니다.");
        }
        return stub;
    }
}
