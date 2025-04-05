package com.meetime.hubspot.exception;

import org.springframework.http.HttpStatus;

public class HubSpotException extends CustomException {

    public HubSpotException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

}