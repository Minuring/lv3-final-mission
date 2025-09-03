package finalmission.presentation.controller;

import finalmission.application.GymService;
import finalmission.domain.AuthInfo;
import finalmission.domain.AuthenticationException;
import finalmission.presentation.request.RegisterGymRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gyms")
@RequiredArgsConstructor
public class GymController {

    private final GymService gymService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
        final AuthInfo authInfo,
        @Valid @RequestBody RegisterGymRequest request
    ) {
        if (authInfo.isAdmin()) {
            gymService.register(request.name(), request.address());
            return;
        }
        throw new AuthenticationException("관리자만 수행할 수 있습니다.");
    }
}
