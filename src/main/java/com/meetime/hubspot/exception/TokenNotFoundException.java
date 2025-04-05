package com.meetime.hubspot.exception;

import org.springframework.http.HttpStatus;

public class TokenNotFoundException extends CustomException {

    public TokenNotFoundException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

}