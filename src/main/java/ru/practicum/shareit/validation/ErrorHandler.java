package ru.practicum.shareit.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.validation.exceptions.*;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)                                      // 404
    public ErrorResponse notFound(final ObjectNotFoundException exc) {
        log.error(exc.getMessage(), exc);
        return new ErrorResponse(exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)                                      // 409
    public ErrorResponse emailRegistered(final EmailRegisteredException exc) {
        log.error(exc.getMessage(), exc);
        return new ErrorResponse(exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)                                    // 404
    public ErrorResponse validationPartOfBody(final ValidateException exc) {
        log.error(exc.getMessage(), exc);
        return new ErrorResponse(exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)                                    // 400
    public ErrorResponse notValidLogicRequest(final ValidException exc) {
        log.error(exc.getMessage(), exc);
        return new ErrorResponse(exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)                                    // 400
    public ErrorResponse badSBValidationUpLevelController(final ConstraintViolationException exc) {
        log.error(exc.getMessage(), exc);
        return new ErrorResponse(exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)                                    // 400
    public ErrorResponse badSBValidationOnRequestBody(final MethodArgumentNotValidException exc) {
        log.error(exc.getMessage(), exc);
        return new ErrorResponse(exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)                          // 500
    public ErrorResponse unsupportedStatus(final UnsupportedException exc) {
        log.error(exc.getMessage(), exc);
        return new ErrorResponse(exc.getMessage());
    }
}