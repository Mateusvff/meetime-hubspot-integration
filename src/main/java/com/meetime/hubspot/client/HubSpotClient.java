package com.meetime.hubspot.client;

import com.meetime.hubspot.domain.auth.ExchangeForTokenResponse;
import com.meetime.hubspot.domain.contact.CreateContactRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "hubspot", url = "https://api.hubapi.com")
public interface HubSpotClient {

    @PostMapping(value = "/oauth/v1/token", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeForTokenResponse exchangeForToken(@RequestParam("grant_type") String grantType,
                                              @RequestParam("client_id") String clientId,
                                              @RequestParam("client_secret") String clientSecret,
                                              @RequestParam("redirect_uri") String redirectUri,
                                              @RequestParam("code") String code);

    @PostMapping(value = "/oauth/v1/token", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeForTokenResponse refreshToken(@RequestParam("grant_type") String grantType,
                                          @RequestParam("client_id") String clientId,
                                          @RequestParam("client_secret") String clientSecret,
                                          @RequestParam("refresh_token") String refreshToken);

    @PostMapping(value = "/crm/v3/objects/contacts", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    void createContact(@RequestHeader("Authorization") String authorizationHeader,
                       @RequestBody CreateContactRequest createContactRequest);

}