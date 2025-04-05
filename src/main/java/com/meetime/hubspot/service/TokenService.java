package com.meetime.hubspot.service;

import com.meetime.hubspot.client.HubSpotClient;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.dto.auth.ExchangeForTokenResponse;
import com.meetime.hubspot.dto.auth.TokenInformation;
import com.meetime.hubspot.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.meetime.hubspot.util.Constants.TOKEN_FILE_PATH;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final HubSpotClient hubSpotClient;
    private final OAuthProperties oAuthProperties;

    public TokenInformation getToken() {
        TokenInformation token = FileUtils.readFromFile(TOKEN_FILE_PATH, TokenInformation.class);

        if (token.isExpired()) {
            refreshToken();
            token = FileUtils.readFromFile(TOKEN_FILE_PATH, TokenInformation.class);
        }

        return token;
    }

    public void refreshToken() {
        logger.info("Refreshing access token");

        TokenInformation tokenInformation = FileUtils.readFromFile(TOKEN_FILE_PATH, TokenInformation.class);
        ExchangeForTokenResponse refreshTokenResponse = callRefreshTokenAPI(tokenInformation);

        FileUtils.writeToFile(TOKEN_FILE_PATH, TokenInformation.fromResponse(refreshTokenResponse));
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

}