package com.meetime.hubspot.service;

import com.meetime.hubspot.client.HubSpotClient;
import com.meetime.hubspot.domain.contact.CreateContactRequest;
import com.meetime.hubspot.exception.HubSpotException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

    @Mock
    private HubSpotClient hubSpotClient;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private ContactService contactService;

    @Test
    void shouldCreateContact() {
        CreateContactRequest createContactRequest = mock(CreateContactRequest.class);
        when(tokenService.getAccessToken()).thenReturn("access_token");

        contactService.createContact(createContactRequest);

        verify(hubSpotClient).createContact("Bearer access_token", createContactRequest);
    }

    @Test
    void shouldThrowHubSpotExceptionWhenCreatingContact() {
        CreateContactRequest createContactRequest = mock(CreateContactRequest.class);

        when(tokenService.getAccessToken()).thenReturn("access_token");

        doThrow(new RuntimeException("Error"))
                .when(hubSpotClient)
                .createContact("Bearer access_token", createContactRequest);

        Assertions.assertThrows(HubSpotException.class, () -> contactService.createContact(createContactRequest));
    }

}