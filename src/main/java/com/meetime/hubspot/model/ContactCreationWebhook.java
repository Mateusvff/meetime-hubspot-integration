package com.meetime.hubspot.model;

import com.meetime.hubspot.domain.webhook.ContactCreatedWebhook;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "contact_creation_webhook")
@NoArgsConstructor
public class ContactCreationWebhook {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "app_id")
    private Long appId;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "portal_id")
    private Long portalId;

    @Column(name = "occurred_at")
    private Long occurredAt;

    @Column(name = "subscription_type")
    private String subscriptionType;

    @Column(name = "attempt_number")
    private Long attemptNumber;

    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "change_source")
    private String changeSource;

    @Column(name = "change_flag")
    private String changeFlag;

    public ContactCreationWebhook(ContactCreatedWebhook contactCreatedWebhook) {
        this.appId = contactCreatedWebhook.appId();
        this.eventId = contactCreatedWebhook.eventId();
        this.subscriptionId = contactCreatedWebhook.subscriptionId();
        this.portalId = contactCreatedWebhook.portalId();
        this.occurredAt = contactCreatedWebhook.occurredAt();
        this.subscriptionType = contactCreatedWebhook.subscriptionType();
        this.attemptNumber = contactCreatedWebhook.attemptNumber();
        this.objectId = contactCreatedWebhook.objectId();
        this.changeSource = contactCreatedWebhook.changeSource();
        this.changeFlag = contactCreatedWebhook.changeFlag();
    }

}