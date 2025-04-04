package com.meetime.hubspot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class OAuthProperties {

    @Value("${oauth.client.id}")
    private String clientId;

    @Value("${oauth.client.secret}")
    private String clientSecret;

    @Value("${redirect.uri}")
    private String redirectUri;

    @Value("${scope}")
    private String scope;

}