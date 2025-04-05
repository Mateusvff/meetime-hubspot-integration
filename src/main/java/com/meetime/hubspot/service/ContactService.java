package com.meetime.hubspot.service;

import com.meetime.hubspot.client.HubSpotClient;
import com.meetime.hubspot.dto.contact.CreateContactRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {

    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

    private final HubSpotClient hubSpotClient;
    private final TokenService tokenService;

    public void createContact(CreateContactRequest createContactRequest) {
        logger.info("Creating contact in CRM");

        String authorizationHeader = "Bearer " + tokenService.getToken().getAccessToken();
        hubSpotClient.createContact(authorizationHeader, createContactRequest);
    }

}