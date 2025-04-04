package com.meetime.hubspot.client;

import com.meetime.hubspot.dto.auth.ExchangeForTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@FeignClient(name = "hubspot", url = "https://api.hubapi.com")
public interface HubSpotClient {

    @PostMapping(value = "/oauth/v1/token", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeForTokenResponse exchangeForToken(@RequestParam("grant_type") String grantType,
                                              @RequestParam("client_id") String clientId,
                                              @RequestParam("client_secret") String clientSecret,
                                              @RequestParam("redirect_uri") String redirectUri,
                                              @RequestParam("code") String code);

}