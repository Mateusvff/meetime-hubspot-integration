package com.meetime.hubspot.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends CustomException {

    public AuthorizationException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

}