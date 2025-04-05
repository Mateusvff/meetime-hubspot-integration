package com.meetime.hubspot.service;

import com.meetime.hubspot.client.HubSpotClient;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.dto.auth.AuthorizationURL;
import com.meetime.hubspot.dto.auth.ExchangeForTokenResponse;
import com.meetime.hubspot.dto.auth.TokenInformation;
import com.meetime.hubspot.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.meetime.hubspot.util.Constants.TOKEN_FILE_PATH;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);

    private final HubSpotClient hubSpotClient;
    private final OAuthProperties oAuthProperties;

    public AuthorizationURL retrieveAuthorizationUrl() {
        logger.info("Generating Authorization URL");
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
            throw new RuntimeException("Error generating authorization URL", e);
        }
    }

    public void handleCallback(String authorizationCode) {
        ExchangeForTokenResponse exchangeForTokenResponse = exchangeForToken(authorizationCode);

        TokenInformation token = TokenInformation.fromResponse(exchangeForTokenResponse);
        FileUtils.writeToFile(TOKEN_FILE_PATH, token);
    }

    private ExchangeForTokenResponse exchangeForToken(String authorizationCode) {
        logger.info("Exchanging authorization code for access token");
        try {
            return hubSpotClient.exchangeForToken(
                    "authorization_code",
                    oAuthProperties.getClientId(),
                    oAuthProperties.getClientSecret(),
                    oAuthProperties.getRedirectUri(),
                    authorizationCode
            );
        } catch (Exception e) {
            throw new RuntimeException("Error while exchanging authorization code for access token", e);
        }
    }

}