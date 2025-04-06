package com.meetime.hubspot.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionTest {

    @Test
    void testCustomException() {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Custom Exception";

        CustomException ex = new CustomException(status, message);

        assertEquals(status, ex.getStatus());
        assertEquals(message, ex.getMessage());
    }

    @Test
    void testAuthorizationException() {
        String message = "Authorization Exception";

        AuthorizationException ex = new AuthorizationException(message);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
        assertEquals(message, ex.getMessage());
    }

    @Test
    void testHubSpotException() {
        String message = "HubSpot Exception";

        HubSpotException ex = new HubSpotException(message);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
        assertEquals(message, ex.getMessage());
    }

    @Test
    void testInvalidWebhookException() {
        String message = "Invalid Webhook Exception";

        InvalidWebhookSignatureException ex = new InvalidWebhookSignatureException(message);

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        assertEquals(message, ex.getMessage());
    }

    @Test
    void testRateLimitException() {
        String message = "Rate Limit Exception";

        RateLimitException ex = new RateLimitException(message);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, ex.getStatus());
        assertEquals(message, ex.getMessage());
    }

    @Test
    void testTokenNotFoundExceptionMessageAndStatus() {
        String message = "Token Not Found Exception";
        TokenNotFoundException ex = new TokenNotFoundException(message);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
        assertEquals(message, ex.getMessage());
    }

}