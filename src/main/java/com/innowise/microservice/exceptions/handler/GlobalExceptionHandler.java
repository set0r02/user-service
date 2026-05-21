package com.innowise.microservice.exceptions.handler;

import com.innowise.microservice.exceptions.AlreadyTakenException;
import com.innowise.microservice.exceptions.EntityNotFoundException;
import com.innowise.microservice.exceptions.MaxPaymentCardsUserException;
import com.innowise.microservice.exceptions.UserAlreadyRegisteredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException entityNotFoundException) {
        log.warn("Resource not found: {}", entityNotFoundException.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                entityNotFoundException.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({
            MaxPaymentCardsUserException.class,
            AlreadyTakenException.class,
            UserAlreadyRegisteredException.class
    })
    public ResponseEntity<ErrorResponse> handleBusinessError(RuntimeException runtimeException){
        log.warn("Business exception occurred: {}", runtimeException.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                runtimeException.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception){
        log.error("An unhandled exception was caught", exception);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected internal server error occurred",
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
