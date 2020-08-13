package wooteco.team.ittabi.legenoaroundhere.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.team.ittabi.legenoaroundhere.dto.ErrorResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.FileIOException;
import wooteco.team.ittabi.legenoaroundhere.exception.MultipartFileConvertException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotAuthorizedException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotFoundAlgorithmException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageExtensionException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageMimeTypeException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotUniqueException;
import wooteco.team.ittabi.legenoaroundhere.exception.WrongUserInputException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(NotExistsException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotExistsException e) {
        e.printStackTrace();
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({WrongUserInputException.class, NotImageMimeTypeException.class,
        NotImageExtensionException.class, MultipartFileConvertException.class,
        FileIOException.class, NotUniqueException.class, PropertyReferenceException.class,
        BindException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception e) {
        e.printStackTrace();

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(NotAuthorizedException e) {
        e.printStackTrace();

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({Exception.class, NotFoundAlgorithmException.class})
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception e) {
        e.printStackTrace();

        log.info(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("예기치 않은 오류가 발생하였습니다."));
    }
}
