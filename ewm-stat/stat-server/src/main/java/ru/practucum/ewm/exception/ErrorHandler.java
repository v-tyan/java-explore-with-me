package ru.practucum.ewm.exception;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ValidationException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({ MethodArgumentNotValidException.class,
            BadRequestException.class,
            ConstraintViolationException.class, ValidationException.class,
            MissingServletRequestParameterException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerBadRequest(final RuntimeException e) {
        log.warn("400 {}", e.getMessage(), e);
        return new ErrorResponse("Object not available 400 ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIntegrityException(final DataAccessException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse("No valid data", e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.info("500 {}", e.getMessage(), e);
        return new ErrorResponse("InternalServerError", e.getMessage());
    }
}
