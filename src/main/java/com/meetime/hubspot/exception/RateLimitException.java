package com.meetime.hubspot.exception;

import org.springframework.http.HttpStatus;

public class RateLimitException extends CustomException {

    public RateLimitException(String message) {
        super(HttpStatus.TOO_MANY_REQUESTS, message);
    }

}