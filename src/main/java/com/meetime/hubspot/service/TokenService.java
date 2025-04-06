package com.meetime.hubspot.service;

import com.meetime.hubspot.client.HubSpotClient;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.domain.auth.ExchangeForTokenResponse;
import com.meetime.hubspot.exception.HubSpotException;
import com.meetime.hubspot.exception.TokenNotFoundException;
import com.meetime.hubspot.model.TokenInformation;
import com.meetime.hubspot.repository.TokenInformationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final HubSpotClient hubSpotClient;
    private final TokenInformationRepository tokenInformationRepository;
    private final OAuthProperties oAuthProperties;

    public String getAccessToken() {
        TokenInformation token = retrieveTokenInformation();

        if (token.isExpired()) {
            token = refreshToken(token);
        }

        return token.getAccessToken();
    }

    public TokenInformation refreshToken(TokenInformation token) {
        log.info("Refreshing access token");

        ExchangeForTokenResponse refreshTokenResponse = callRefreshTokenAPI(token);
        var refreshedToken = token.update(refreshTokenResponse);

        tokenInformationRepository.save(refreshedToken);
        return refreshedToken;
    }

    private ExchangeForTokenResponse callRefreshTokenAPI(TokenInformation token) {
        try {
            return hubSpotClient.refreshToken(
                    "refresh_token",
                    oAuthProperties.getClientId(),
                    oAuthProperties.getClientSecret(),
                    token.getRefreshToken()
            );
        } catch (Exception e) {
            log.error("Error while calling refresh token API", e);
            throw new HubSpotException("Error while calling refresh token API");
        }
    }

    private TokenInformation retrieveTokenInformation() {
        log.info("Retrieving latest token information");

        return tokenInformationRepository.findTopByOrderByExpiresAtDesc()
                .orElseThrow(() -> new TokenNotFoundException("Token not found"));
    }

}