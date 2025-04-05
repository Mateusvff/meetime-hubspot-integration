package com.meetime.hubspot.service;

import com.meetime.hubspot.domain.webhook.ContactCreatedWebhook;
import com.meetime.hubspot.model.ContactCreationWebhook;
import com.meetime.hubspot.repository.ContactCreationWebhookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final ContactCreationWebhookRepository contactCreationWebhookRepository;

    public void handleWebhook(ContactCreatedWebhook contactCreatedWebhook) {
        log.info("Webhook received: Saving data in database");
        contactCreationWebhookRepository.save(new ContactCreationWebhook(contactCreatedWebhook));
    }

}