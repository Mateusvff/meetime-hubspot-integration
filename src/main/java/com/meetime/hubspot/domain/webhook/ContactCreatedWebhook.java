package com.meetime.hubspot.domain.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ContactCreatedWebhook(Long appId,
                                    Long eventId,
                                    Long subscriptionId,
                                    Long portalId,
                                    Long occurredAt,
                                    String subscriptionType,
                                    Long attemptNumber,
                                    Long objectId,
                                    String changeSource,
                                    String changeFlag) {

}