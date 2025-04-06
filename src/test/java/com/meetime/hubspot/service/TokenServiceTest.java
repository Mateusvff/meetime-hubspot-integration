package com.meetime.hubspot.service;

import com.meetime.hubspot.client.HubSpotClient;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.domain.auth.ExchangeForTokenResponse;
import com.meetime.hubspot.exception.HubSpotException;
import com.meetime.hubspot.exception.TokenNotFoundException;
import com.meetime.hubspot.model.TokenInformation;
import com.meetime.hubspot.repository.TokenInformationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private HubSpotClient hubSpotClient;

    @Mock
    private TokenInformationRepository tokenInformationRepository;

    @Mock
    private OAuthProperties oAuthProperties;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void shouldGetAccessToken() {
        TokenInformation token = mock(TokenInformation.class);

        when(tokenInformationRepository.findTopByOrderByExpiresAtDesc()).thenReturn(Optional.of(token));
        when(token.isExpired()).thenReturn(false);
        when(token.getAccessToken()).thenReturn("access_token");

        String accessToken = tokenService.getAccessToken();

        assertEquals("access_token", accessToken);
    }

    @Test
    void shouldGetAccessTokenWithRefreshToken() {
        TokenInformation expiredToken = mock(TokenInformation.class);
        TokenInformation refreshedToken = mock(TokenInformation.class);
        ExchangeForTokenResponse exchangeForTokenResponse = new ExchangeForTokenResponse("accessToken", "refreshToken", 3600);

        when(tokenInformationRepository.findTopByOrderByExpiresAtDesc()).thenReturn(Optional.of(expiredToken));
        when(expiredToken.isExpired()).thenReturn(true);
        when(hubSpotClient.refreshToken(any(), any(), any(), any())).thenReturn(exchangeForTokenResponse);
        when(expiredToken.update(exchangeForTokenResponse)).thenReturn(refreshedToken);
        when(refreshedToken.getAccessToken()).thenReturn("accessToken");

        String accessToken = tokenService.getAccessToken();

        assertEquals("accessToken", accessToken);
        verify(tokenInformationRepository).save(refreshedToken);
    }

    @Test
    void shouldThrowHubSpotExceptionWhenRefreshingAccessToken() {
        TokenInformation expiredToken = mock(TokenInformation.class);

        when(tokenInformationRepository.findTopByOrderByExpiresAtDesc()).thenReturn(Optional.of(expiredToken));
        when(expiredToken.isExpired()).thenReturn(true);
        when(hubSpotClient.refreshToken(any(), any(), any(), any())).thenThrow(new RuntimeException("Error"));

        Assertions.assertThrows(HubSpotException.class, tokenService::getAccessToken);
    }

    @Test
    void shouldThrowTokenNotFoundExceptionWhenNoTokenFound() {
        when(tokenInformationRepository.findTopByOrderByExpiresAtDesc()).thenReturn(Optional.empty());

        Assertions.assertThrows(TokenNotFoundException.class, tokenService::getAccessToken);
    }

    @Test
    void shouldRefreshToken() {

    }


}