package com.meetime.hubspot.domain.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthorizationURL(@JsonProperty("authorization_url") String authorizationUrl) {

}