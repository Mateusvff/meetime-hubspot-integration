package com.meetime.hubspot.controller;

import com.meetime.hubspot.domain.auth.AuthorizationURL;
import com.meetime.hubspot.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping(value = "/authorize", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "API responsible for generating and returning the authorization URL.")
    public ResponseEntity<AuthorizationURL> generateAuthorizationUrl() {
        return ResponseEntity.ok().body(oAuthService.retrieveAuthorizationUrl());
    }

    @GetMapping(value = "/callback", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "API responsible for receiving the authorization code provided by HubSpot and exchanging it for an access token.")
    public ResponseEntity<Void> callback(@RequestParam("code") String authorizationCode) {
        oAuthService.handleCallback(authorizationCode);
        return ResponseEntity.ok().build();
    }

}