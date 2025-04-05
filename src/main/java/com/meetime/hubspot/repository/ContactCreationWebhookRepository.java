package com.meetime.hubspot.repository;

import com.meetime.hubspot.model.ContactCreationWebhook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContactCreationWebhookRepository extends JpaRepository<ContactCreationWebhook, UUID> {
}