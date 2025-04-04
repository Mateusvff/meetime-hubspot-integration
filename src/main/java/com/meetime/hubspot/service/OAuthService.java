package com.meetime.hubspot.service;

import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.dto.auth.AuthorizationURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class OAuthService {

    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);

    private final OAuthProperties oAuthProperties;

    public OAuthService(OAuthProperties oAuthProperties) {
        this.oAuthProperties = oAuthProperties;
    }

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

}