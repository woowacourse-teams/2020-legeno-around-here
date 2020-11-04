package wooteco.team.ittabi.legenoaroundhere.controller;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.team.ittabi.legenoaroundhere.dto.ErrorResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.AlreadyExistUserException;
import wooteco.team.ittabi.legenoaroundhere.exception.FileIOException;
import wooteco.team.ittabi.legenoaroundhere.exception.LoginPageRedirectException;
import wooteco.team.ittabi.legenoaroundhere.exception.MultipartFileConvertException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotFoundAlgorithmException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageExtensionException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageMimeTypeException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotUniqueException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotExistsException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotExistsException e) {
        log.info("NotFound Exception : ", e);

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({WrongUserInputException.class, NotImageMimeTypeException.class,
        NotImageExtensionException.class, MultipartFileConvertException.class,
        FileIOException.class, NotUniqueException.class, PropertyReferenceException.class,
        BindException.class, AlreadyExistUserException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception e) {
        log.info("BadRequest Exception : ", e);

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
        ConstraintViolationException constraintViolationException) {
        String errorMessage = getErrorMessage(constraintViolationException);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(errorMessage));
    }

    private String getErrorMessage(ConstraintViolationException constraintViolationException) {
        Set<ConstraintViolation<?>> violations = constraintViolationException
            .getConstraintViolations();
        violations.forEach(violation -> log.info(violation.getMessage()));
        String errorMessage = "ConstraintViolationException occured";

        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            violations.forEach(violation -> builder.append(violation.getMessage()));
            errorMessage = builder.toString();
        }
        return errorMessage;
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(NotAuthorizedException e) {
        log.info("Forbidden Exception : ", e);

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(LoginPageRedirectException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized() {

        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse("Login이 필요합니다."));
    }

    @ExceptionHandler({Exception.class, NotFoundAlgorithmException.class})
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception e) {
        log.error("InternalServerError : ", e);

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("예기치 않은 오류가 발생하였습니다."));
    }
}
