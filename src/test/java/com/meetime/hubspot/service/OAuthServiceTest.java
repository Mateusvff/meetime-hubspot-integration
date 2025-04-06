package com.meetime.hubspot.service;

import com.meetime.hubspot.client.HubSpotClient;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.domain.auth.ExchangeForTokenResponse;
import com.meetime.hubspot.exception.HubSpotException;
import com.meetime.hubspot.model.TokenInformation;
import com.meetime.hubspot.repository.TokenInformationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OAuthServiceTest {

    @Mock
    private HubSpotClient hubSpotClient;

    @Mock
    private TokenInformationRepository tokenInformationRepository;

    @Mock
    private OAuthProperties oAuthProperties;

    @InjectMocks
    private OAuthService oAuthService;

    @Test
    void shouldRetrieveAuthorizationUrl() {
        when(oAuthProperties.getRedirectUri()).thenReturn("http://localhost:8080/test-callback");
        when(oAuthProperties.getClientId()).thenReturn("client_id");
        when(oAuthProperties.getScope()).thenReturn("test_scope");

        String expectedUrl = "https://app.hubspot.com/oauth/authorize?client_id=client_id&redirect_uri=http://localhost:8080/test-callback&scope=test_scope";
        String actualUrl = oAuthService.retrieveAuthorizationUrl().authorizationUrl();

        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void shouldHandleCallback() {
        ExchangeForTokenResponse exchangeForTokenResponse = mock(ExchangeForTokenResponse.class);

        when(oAuthProperties.getClientId()).thenReturn("client_id");
        when(oAuthProperties.getClientSecret()).thenReturn("client_secret");
        when(oAuthProperties.getRedirectUri()).thenReturn("http://localhost:8080/test-callback");
        when(hubSpotClient.exchangeForToken(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(exchangeForTokenResponse);

        String authorizationCode = "authorization_code";
        oAuthService.handleCallback(authorizationCode);

        verify(tokenInformationRepository).save(any(TokenInformation.class));
    }

    @Test
    void shouldThrowHubSpotExceptionWhenExchangingForToken() {
        when(oAuthProperties.getClientId()).thenReturn("client_id");
        when(oAuthProperties.getClientSecret()).thenReturn("client_secret");
        when(oAuthProperties.getRedirectUri()).thenReturn("http://localhost:8080/test-callback");
        when(hubSpotClient.exchangeForToken(anyString(), anyString(), anyString(), anyString(), anyString())).thenThrow(new RuntimeException("Error"));

        String authorizationCode = "authorization_code";

        assertThrows(HubSpotException.class, () -> oAuthService.handleCallback(authorizationCode));
    }

}