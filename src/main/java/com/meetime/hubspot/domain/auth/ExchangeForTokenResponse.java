package com.meetime.hubspot.domain.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeForTokenResponse(@JsonProperty("access_token") String accessToken,
                                       @JsonProperty("refresh_token") String refreshToken,
                                       @JsonProperty("expires_in") Integer expiresIn) {

}