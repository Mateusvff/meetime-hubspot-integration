package com.meetime.hubspot.domain.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record TokenInformation(String accessToken, String refreshToken, Integer expiresIn, Instant expiresAt) {

    public TokenInformation(ExchangeForTokenResponse token) {
        this(token.accessToken(), token.refreshToken(), token.expiresIn(), Instant.now().plusSeconds(token.expiresIn() - 60));
    }

    public boolean isExpired() {
        return expiresAt == null || Instant.now().isAfter(expiresAt);
    }

}