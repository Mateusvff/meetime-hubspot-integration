package com.meetime.hubspot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "contact_creation_webhook")
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

}