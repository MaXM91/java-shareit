package ru.practicum.shareit.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
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
}