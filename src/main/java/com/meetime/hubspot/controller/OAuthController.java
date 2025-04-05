package com.meetime.hubspot.controller;

import com.meetime.hubspot.domain.ErrorResponse;
import com.meetime.hubspot.domain.auth.AuthorizationURL;
import com.meetime.hubspot.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "API responsible for generating and returning the authorization URL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authorization URL generated successfully", content = @Content(schema = @Schema(implementation = AuthorizationURL.class))),
            @ApiResponse(responseCode = "405", description = "Method not allowed", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/authorize", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorizationURL> generateAuthorizationUrl() {
        return ResponseEntity.ok().body(oAuthService.retrieveAuthorizationUrl());
    }

    @Operation(summary = "API responsible for receiving the authorization code provided by HubSpot and exchanging it for an access token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token exchanged successfully", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Missing or invalid authorization code", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "405", description = "Method not allowed", content = @Content()),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/callback", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> callback(@RequestParam("code") String authorizationCode) {
        oAuthService.handleCallback(authorizationCode);
        return ResponseEntity.ok().build();
    }

}