package com.meetime.hubspot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meetime.hubspot.client.HubSpotClient;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.dto.auth.ExchangeForTokenResponse;
import com.meetime.hubspot.dto.auth.TokenInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(WRITE_DATES_AS_TIMESTAMPS);
    private static final String TOKEN_FILE = "tokens.json";

    private final HubSpotClient hubSpotClient;
    private final OAuthProperties oAuthProperties;

    public TokenService(HubSpotClient hubSpotClient, OAuthProperties oAuthProperties) {
        this.hubSpotClient = hubSpotClient;
        this.oAuthProperties = oAuthProperties;
    }

    public TokenInformation getToken() {
        TokenInformation token = readFromFile();

        if (token.isExpired()) {
            refreshToken();
            token = readFromFile();
        }

        return token;
    }

    public void refreshToken() {
        logger.info("Refreshing access token");

        TokenInformation tokenInformation = readFromFile();
        ExchangeForTokenResponse refreshTokenResponse = callRefreshTokenAPI(tokenInformation);

        writeToFile(TokenInformation.fromResponse(refreshTokenResponse));
    }

    private ExchangeForTokenResponse callRefreshTokenAPI(TokenInformation tokenInformation) {
        try {
            return hubSpotClient.refreshToken(
                    "refresh_token",
                    oAuthProperties.getClientId(),
                    oAuthProperties.getClientSecret(),
                    tokenInformation.getRefreshToken()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error while calling refresh token API", e);
        }
    }

    private TokenInformation readFromFile() {
        try {
            File tokenFile = new File(TOKEN_FILE);
            if (!tokenFile.exists()) throw new RuntimeException("Token file does not exist");

            return mapper.readValue(tokenFile, TokenInformation.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to retrieve token from token file", e);
        }
    }

    public void writeToFile(TokenInformation token) {
        try {
            mapper.writeValue(new File(TOKEN_FILE), token);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save token to token file", e);
        }
    }

}