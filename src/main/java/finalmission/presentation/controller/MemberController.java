package finalmission.presentation.controller;

import finalmission.application.MemberService;
import finalmission.presentation.request.LoginRequest;
import finalmission.presentation.request.RegisterMemberRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterMemberRequest request) {
        memberService.register(request.id(), request.password(), request.name());
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        var authInfo = memberService.login(request.id(), request.password());
        var tokenCookie = new Cookie("token", authInfo);
        response.addCookie(tokenCookie);
    }
}


