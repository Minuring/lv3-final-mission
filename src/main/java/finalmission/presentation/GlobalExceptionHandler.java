package finalmission.presentation;

import finalmission.domain.AuthenticationException;
import finalmission.exception.BusinessRuleException;
import finalmission.exception.DuplicatedException;
import finalmission.exception.ElementNotFoundException;
import finalmission.exception.InvalidArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@RestControllerAdvice
public class GlobalExceptionHandler extends DefaultHandlerExceptionResolver {

    @ExceptionHandler(BindException.class)
    public ProblemDetail handleBindException(final BindException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "유효하지 않은 요청 값입니다.");
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ProblemDetail handleInvalidArgumentException(final InvalidArgumentException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRuleException(final BusinessRuleException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(final AuthenticationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ProblemDetail handleElementNotFoundException(final ElementNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DuplicatedException.class)
    public ProblemDetail handleDuplicatedException(final DuplicatedException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(final Exception e) {
        logger.error("예상치 못한 예외가 발생했습니다 : ", e);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "서버에서 오류가 발생했습니다.");
    }
}
