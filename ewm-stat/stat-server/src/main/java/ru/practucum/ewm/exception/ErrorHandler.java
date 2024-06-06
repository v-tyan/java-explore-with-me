package ru.practucum.ewm.exception;

import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerBadRequest(final BadRequestException e) {
        log.warn("404 {}", e.getMessage(), e);
        return new ErrorResponse("Object not available 400 ", e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIntegrityException(final DataAccessException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse("No valid data", e.getMessage());
    }
}

