package com.meetime.hubspot.exception;

import org.springframework.http.HttpStatus;

public class InvalidWebhookSignatureException extends CustomException {

    public InvalidWebhookSignatureException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

}