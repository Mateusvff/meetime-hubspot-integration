package com.meetime.hubspot.service;

import com.meetime.hubspot.client.HubSpotClient;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.dto.auth.AuthorizationURL;
import com.meetime.hubspot.dto.auth.ExchangeForTokenResponse;
import com.meetime.hubspot.dto.auth.TokenInformation;
import com.meetime.hubspot.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.meetime.hubspot.util.Constants.TOKEN_FILE_PATH;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final HubSpotClient hubSpotClient;
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
            throw new RuntimeException("Error generating authorization URL", e);
        }
    }

    public void handleCallback(String authorizationCode) {
        ExchangeForTokenResponse exchangeForTokenResponse = exchangeForToken(authorizationCode);

        TokenInformation token = TokenInformation.fromResponse(exchangeForTokenResponse);
        FileUtils.writeToFile(TOKEN_FILE_PATH, token);
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
            throw new RuntimeException("Error while exchanging authorization code for access token", e);
        }
    }

}