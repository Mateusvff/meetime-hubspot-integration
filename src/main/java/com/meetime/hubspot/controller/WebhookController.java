package com.meetime.hubspot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.domain.ErrorResponse;
import com.meetime.hubspot.domain.webhook.ContactCreatedWebhook;
import com.meetime.hubspot.exception.InvalidWebhookSignatureException;
import com.meetime.hubspot.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final OAuthProperties oAuthProperties;
    private final WebhookService webhookService;

    @Operation(summary = "API responsible for receiving the webhook from HubSpot and processing it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Webhook processed successfully", content = @Content()),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/receive")
    public ResponseEntity<Void> receiveWebhook(@RequestHeader("X-HubSpot-Signature") String signature,
                                            @RequestBody String requestBody) throws JsonProcessingException {
        String clientSecret = oAuthProperties.getClientSecret();
        String expectedSignature = DigestUtils.sha256Hex(clientSecret + requestBody);

        if (!expectedSignature.equals(signature)) {
            throw new InvalidWebhookSignatureException("Invalid webhook signature");
        }

        ContactCreatedWebhook[] webhooks = new ObjectMapper().readValue(requestBody, ContactCreatedWebhook[].class);

        for (ContactCreatedWebhook contactCreatedWebhook : webhooks) {
            webhookService.handleWebhook(contactCreatedWebhook);
        }

        return ResponseEntity.ok().build();
    }

}