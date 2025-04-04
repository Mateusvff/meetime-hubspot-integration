package com.meetime.hubspot.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorizationURL {

    @JsonProperty("authorization_url")
    private String authorizationUrl;

}