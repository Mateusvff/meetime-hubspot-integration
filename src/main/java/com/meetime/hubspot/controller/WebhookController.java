package com.meetime.hubspot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetime.hubspot.config.OAuthProperties;
import com.meetime.hubspot.domain.webhook.ContactCreatedWebhook;
import com.meetime.hubspot.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.ietf.jgss.GSSException.UNAUTHORIZED;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final OAuthProperties oAuthProperties;
    private final WebhookService webhookService;

    @PostMapping("/receive")
    @Operation(summary = "API responsible for receiving the webhook from HubSpot and processing it.")
    public ResponseEntity<?> receiveWebhook(@RequestHeader("X-HubSpot-Signature") String signature,
                                            @RequestBody String requestBody) throws JsonProcessingException {
        String clientSecret = oAuthProperties.getClientSecret();
        String expectedSignature = DigestUtils.sha256Hex(clientSecret + requestBody);

        if (!expectedSignature.equals(signature)) {
            return ResponseEntity.status(UNAUTHORIZED).body("Invalid webhook signature");
        }

        ContactCreatedWebhook[] webhooks = new ObjectMapper().readValue(requestBody, ContactCreatedWebhook[].class);

        for (ContactCreatedWebhook contactCreatedWebhook : webhooks) {
            webhookService.handleWebhook(contactCreatedWebhook);
        }

        return ResponseEntity.ok().build();
    }

}