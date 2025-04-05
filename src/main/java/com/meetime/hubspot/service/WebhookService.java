package com.meetime.hubspot.service;

import com.meetime.hubspot.dto.webhook.ContactCreatedWebhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    private static final Logger logger = LoggerFactory.getLogger(WebhookService.class);

    public void handleWebhook(ContactCreatedWebhook contactCreatedWebhook) {
        logger.info("Webhook received: {}", contactCreatedWebhook.toString());
    }

}