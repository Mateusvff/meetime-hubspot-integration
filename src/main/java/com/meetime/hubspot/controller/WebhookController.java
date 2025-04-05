package com.meetime.hubspot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetime.hubspot.dto.webhook.ContactCreatedWebhook;
import com.meetime.hubspot.service.WebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/receive")
    public ResponseEntity<?> receiveWebhook(@RequestBody String requestBody) throws JsonProcessingException {
        ContactCreatedWebhook[] webhooks = new ObjectMapper().readValue(requestBody, ContactCreatedWebhook[].class);

        for (ContactCreatedWebhook contactCreatedWebhook : webhooks) {
            webhookService.handleWebhook(contactCreatedWebhook);
        }

        return ResponseEntity.ok().build();
    }

}