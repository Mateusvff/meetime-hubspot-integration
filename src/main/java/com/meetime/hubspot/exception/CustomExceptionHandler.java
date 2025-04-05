package com.meetime.hubspot.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(new ErrorResponse(Instant.now(), ex.getStatus().value(), ex.getStatus().getReasonPhrase(), ex.getMessage()));
    }

    public record ErrorResponse(Instant timestamp, int status, String error, String message) {
    }

}