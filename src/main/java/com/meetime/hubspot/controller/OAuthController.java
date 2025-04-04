package com.meetime.hubspot.controller;

import com.meetime.hubspot.dto.auth.AuthorizationURL;
import com.meetime.hubspot.service.OAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping(value = "/authorize", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorizationURL> generateAuthorizationUrl() {
        return ResponseEntity.ok().body(oAuthService.retrieveAuthorizationUrl());
    }

}