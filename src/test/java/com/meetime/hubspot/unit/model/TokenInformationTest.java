package com.meetime.hubspot.unit.model;

import com.meetime.hubspot.domain.auth.ExchangeForTokenResponse;
import com.meetime.hubspot.model.TokenInformation;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenInformationTest {

    @Test
    void testCalculateExpiresAt() {
        int expiresIn = 3600;

        TokenInformation token = new TokenInformation("access_token", "refresh_token", expiresIn);

        assertNotNull(token.getExpiresAt());
    }

    @Test
    void testIsExpiredReturnsTrueWhenExpired() {
        TokenInformation token = new TokenInformation();
        token.setExpiresAt(Instant.now().minusSeconds(10));

        assertTrue(token.isExpired());
    }

    @Test
    void testIsExpiredReturnsFalseWhenNotExpired() {
        TokenInformation token = new TokenInformation();
        token.setExpiresAt(Instant.now().plusSeconds(300));

        assertFalse(token.isExpired());
    }

    @Test
    void testIsExpiredReturnsTrueWhenExpiresAtIsNull() {
        TokenInformation token = new TokenInformation();
        token.setExpiresAt(null);

        assertTrue(token.isExpired());
    }

    @Test
    void testConstructorSetsFieldsCorrectly() {
        String accessToken = "access_token";
        String refreshToken = "refresh_token";
        int expiresIn = 1800;

        TokenInformation token = new TokenInformation(accessToken, refreshToken, expiresIn);

        assertEquals(accessToken, token.getAccessToken());
        assertEquals(refreshToken, token.getRefreshToken());
        assertEquals(expiresIn, token.getExpiresIn());
    }

    @Test
    void testUpdateMethodUpdatesFieldsCorrectly() {
        TokenInformation token = new TokenInformation();
        ExchangeForTokenResponse response = mock(ExchangeForTokenResponse.class);

        when(response.accessToken()).thenReturn("new_access");
        when(response.refreshToken()).thenReturn("new_refresh");
        when(response.expiresIn()).thenReturn(1800);

        token.update(response);

        assertEquals("new_access", token.getAccessToken());
        assertEquals("new_refresh", token.getRefreshToken());
        assertEquals(1800, token.getExpiresIn());
        assertNotNull(token.getExpiresAt());
    }

}