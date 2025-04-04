package com.meetime.hubspot.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenInformation {

    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
    private Instant expiresAt;

    public static TokenInformation fromResponse(ExchangeForTokenResponse response) {
        TokenInformation token = new TokenInformation();
        token.setAccessToken(response.getAccessToken());
        token.setRefreshToken(response.getRefreshToken());
        token.setExpiresIn(response.getExpiresIn());
        token.setExpiresAt(Instant.now().plusSeconds(response.getExpiresIn() - 60));
        return token;
    }

    public boolean isExpired() {
        return expiresAt == null || Instant.now().isAfter(expiresAt);
    }

}