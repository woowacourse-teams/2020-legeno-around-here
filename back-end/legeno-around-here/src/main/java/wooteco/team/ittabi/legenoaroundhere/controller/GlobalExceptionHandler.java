package wooteco.team.ittabi.legenoaroundhere.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.team.ittabi.legenoaroundhere.dto.ErrorResponse;
import wooteco.team.ittabi.legenoaroundhere.exception.NotExistsException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageExtensionException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageMimeTypeException;
import wooteco.team.ittabi.legenoaroundhere.exception.UserInputException;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotExistsException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotExistsException e) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({UserInputException.class, NotImageMimeTypeException.class,
        NotImageExtensionException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(UserInputException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception e) {
        logger.info(e.getMessage());
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("예기치 않은 오류가 발생하였습니다."));
    }
}
