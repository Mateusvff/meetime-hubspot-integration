package com.meetime.hubspot.service;

import com.meetime.hubspot.domain.webhook.ContactCreatedWebhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebhookService {

    public void handleWebhook(ContactCreatedWebhook contactCreatedWebhook) {
        log.info("Webhook received: {}", contactCreatedWebhook.toString());
    }

}