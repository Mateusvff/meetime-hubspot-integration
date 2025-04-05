package com.meetime.hubspot.service;

import com.meetime.hubspot.client.HubSpotClient;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.domain.auth.AuthorizationURL;
import com.meetime.hubspot.domain.auth.ExchangeForTokenResponse;
import com.meetime.hubspot.exception.AuthorizationException;
import com.meetime.hubspot.exception.HubSpotException;
import com.meetime.hubspot.model.TokenInformation;
import com.meetime.hubspot.repository.TokenInformationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final HubSpotClient hubSpotClient;
    private final TokenInformationRepository tokenInformationRepository;
    private final OAuthProperties oAuthProperties;

    public AuthorizationURL retrieveAuthorizationUrl() {
        log.info("Generating Authorization URL");
        return new AuthorizationURL(buildAuthorizationUrl());
    }

    private String buildAuthorizationUrl() {
        try {
            return UriComponentsBuilder
                    .fromUri(URI.create("https://app.hubspot.com/oauth/authorize"))
                    .queryParam("client_id", oAuthProperties.getClientId())
                    .queryParam("redirect_uri", oAuthProperties.getRedirectUri())
                    .queryParam("scope", oAuthProperties.getScope())
                    .build().encode().toUriString();
        } catch (Exception e) {
            log.error("Error generating authorization URL", e);
            throw new AuthorizationException("Error generating authorization URL");
        }
    }

    public void handleCallback(String authorizationCode) {
        var token = exchangeForToken(authorizationCode);
        tokenInformationRepository.save(new TokenInformation(token.accessToken(), token.refreshToken(), token.expiresIn()));
    }

    private ExchangeForTokenResponse exchangeForToken(String authorizationCode) {
        log.info("Exchanging authorization code for access token");
        try {
            return hubSpotClient.exchangeForToken(
                    "authorization_code",
                    oAuthProperties.getClientId(),
                    oAuthProperties.getClientSecret(),
                    oAuthProperties.getRedirectUri(),
                    authorizationCode
            );
        } catch (Exception e) {
            log.error("Error while exchanging authorization code for access token", e);
            throw new HubSpotException("Error while exchanging authorization code for access token");
        }
    }

}