package com.meetime.hubspot.unit.service;

import com.meetime.hubspot.domain.webhook.ContactCreatedWebhook;
import com.meetime.hubspot.model.ContactCreationWebhook;
import com.meetime.hubspot.repository.ContactCreationWebhookRepository;
import com.meetime.hubspot.service.WebhookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WebhookServiceTest {

    @Mock
    private ContactCreationWebhookRepository contactCreationWebhookRepository;

    @InjectMocks
    private WebhookService webhookService;

    @Test
    void shouldHandleWebhook() {
        ContactCreatedWebhook contactCreatedWebhook = mock(ContactCreatedWebhook.class);

        webhookService.handleWebhook(contactCreatedWebhook);

        verify(contactCreationWebhookRepository).save(any(ContactCreationWebhook.class));
    }

}