package finalmission.presentation;

import finalmission.domain.AuthInfo;
import finalmission.domain.AuthenticationException;
import finalmission.domain.member.MemberTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberTokenProvider memberTokenProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        var type = parameter.getParameterType();
        return AuthInfo.class.isAssignableFrom(type);
    }

    @Override
    public Object resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) {
        var request = (HttpServletRequest) webRequest.getNativeRequest();
        var cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthenticationException("로그인이 필요합니다.");
        }

        return extractAuthInfoFromCookies(cookies);
    }

    private AuthInfo extractAuthInfoFromCookies(final Cookie[] cookies) {
        return Arrays.stream(cookies)
            .filter(c -> c.getName().equals("token"))
            .map(Cookie::getValue)
            .map(memberTokenProvider::extractAuthInfo)
            .findAny()
            .orElseThrow(() -> new AuthenticationException("로그인이 필요합니다."));
    }
}
