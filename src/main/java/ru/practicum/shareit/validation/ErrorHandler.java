package ru.practicum.shareit.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.validation.exception.EmailRegisteredException;
import ru.practicum.shareit.validation.exception.ErrorResponse;
import ru.practicum.shareit.validation.exception.ObjectNotFoundException;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)                                      // 404
    public ErrorResponse notFound(final ObjectNotFoundException exc) {
        getLog(exc.getStackTrace(), exc.getMessage());
        return new ErrorResponse(getClassStartException(exc.getStackTrace()) + "/" +
                getMethodStartException(exc.getStackTrace()) + exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)                                      // 409
    public ErrorResponse emailRegistered(final EmailRegisteredException exc) {
        getLog(exc.getStackTrace(), exc.getMessage());
        return new ErrorResponse(getClassStartException(exc.getStackTrace()) + "/" +
                getMethodStartException(exc.getStackTrace()) + exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)                                    // 400
    public ErrorResponse badSBValidationUpLevelController(final ConstraintViolationException exc) {
        getLog(exc.getStackTrace(), exc.getMessage());
        return new ErrorResponse(getClassStartException(exc.getStackTrace()) + "/" +
                getMethodStartException(exc.getStackTrace()) + exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)                                    // 400
    public ErrorResponse badSBValidationOnRequestBody(final MethodArgumentNotValidException exc) {
        getLog(exc.getStackTrace(), exc.getMessage());
        return new ErrorResponse(getClassStartException(exc.getStackTrace()) + "/" +
                getMethodStartException(exc.getStackTrace()) + exc.getMessage());
    }

    private String getClassStartException(StackTraceElement[] methods) {
        return methods[3].toString();
    }

    private String getMethodStartException(StackTraceElement[] methods) {
        return methods[2].toString();
    }

    private void getLog(StackTraceElement[] methods, String message) {
        log.info(methods[3].toString() + "/" + methods[2].toString() + ":" + message);
    }
}
