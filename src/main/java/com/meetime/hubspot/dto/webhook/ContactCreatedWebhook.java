package com.meetime.hubspot.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactCreatedWebhook {

    private Long appId;
    private Long eventId;
    private Long subscriptionId;
    private Long portalId;
    private Long occurredAt;
    private String subscriptionType;
    private Long attemptNumber;
    private Long objectId;
    private String changeSource;
    private String changeFlag;

}