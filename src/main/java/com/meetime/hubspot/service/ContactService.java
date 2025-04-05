package com.meetime.hubspot.service;

import com.meetime.hubspot.client.HubSpotClient;
import com.meetime.hubspot.domain.contact.CreateContactRequest;
import com.meetime.hubspot.exception.HubSpotException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {

    private final HubSpotClient hubSpotClient;
    private final TokenService tokenService;

    public void createContact(CreateContactRequest createContactRequest) {
        log.info("Creating contact in CRM");
        String authorizationHeader = "Bearer " + tokenService.getToken().accessToken();

        try {
            hubSpotClient.createContact(authorizationHeader, createContactRequest);
        } catch (Exception e) {
            log.error("Error while creating contact in CRM", e);
            throw new HubSpotException("Error while creating contact in CRM");
        }
    }

}