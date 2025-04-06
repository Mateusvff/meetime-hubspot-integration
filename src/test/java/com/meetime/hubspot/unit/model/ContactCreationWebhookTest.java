package com.meetime.hubspot.unit.model;

import com.meetime.hubspot.domain.webhook.ContactCreatedWebhook;
import com.meetime.hubspot.model.ContactCreationWebhook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContactCreationWebhookTest {

    @Test
    void testConstructorWithContactCreatedWebhook() {
        ContactCreatedWebhook mockWebhook = mock(ContactCreatedWebhook.class);

        when(mockWebhook.appId()).thenReturn(1L);
        when(mockWebhook.eventId()).thenReturn(2L);
        when(mockWebhook.subscriptionId()).thenReturn(3L);
        when(mockWebhook.portalId()).thenReturn(4L);
        when(mockWebhook.occurredAt()).thenReturn(1234567890L);
        when(mockWebhook.subscriptionType()).thenReturn("contact.creation");
        when(mockWebhook.attemptNumber()).thenReturn(1L);
        when(mockWebhook.objectId()).thenReturn(999L);
        when(mockWebhook.changeSource()).thenReturn("API");
        when(mockWebhook.changeFlag()).thenReturn("NEW");

        ContactCreationWebhook entity = new ContactCreationWebhook(mockWebhook);

        assertEquals(1L, entity.getAppId());
        assertEquals(2L, entity.getEventId());
        assertEquals(3L, entity.getSubscriptionId());
        assertEquals(4L, entity.getPortalId());
        assertEquals(1234567890L, entity.getOccurredAt());
        assertEquals("contact.creation", entity.getSubscriptionType());
        assertEquals(1L, entity.getAttemptNumber());
        assertEquals(999L, entity.getObjectId());
        assertEquals("API", entity.getChangeSource());
        assertEquals("NEW", entity.getChangeFlag());
    }

}