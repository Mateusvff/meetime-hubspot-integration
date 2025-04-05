package com.meetime.hubspot.service;

import com.meetime.hubspot.client.HubSpotClient;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.domain.auth.ExchangeForTokenResponse;
import com.meetime.hubspot.domain.auth.TokenInformation;
import com.meetime.hubspot.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.meetime.hubspot.util.Constants.TOKEN_FILE_PATH;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

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
        log.info("Refreshing access token");

        TokenInformation tokenInformation = FileUtils.readFromFile(TOKEN_FILE_PATH, TokenInformation.class);
        ExchangeForTokenResponse refreshTokenResponse = callRefreshTokenAPI(tokenInformation);

        FileUtils.writeToFile(TOKEN_FILE_PATH, new TokenInformation(refreshTokenResponse));
    }

    private ExchangeForTokenResponse callRefreshTokenAPI(TokenInformation tokenInformation) {
        try {
            return hubSpotClient.refreshToken(
                    "refresh_token",
                    oAuthProperties.getClientId(),
                    oAuthProperties.getClientSecret(),
                    tokenInformation.refreshToken()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error while calling refresh token API", e);
        }
    }

}