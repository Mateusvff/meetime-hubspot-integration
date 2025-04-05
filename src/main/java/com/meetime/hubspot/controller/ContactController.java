package com.meetime.hubspot.controller;

import com.meetime.hubspot.dto.contact.CreateContactRequest;
import com.meetime.hubspot.service.ContactService;
import com.meetime.hubspot.service.RateLimiterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactService contactService;
    private final RateLimiterService rateLimiterService;

    public ContactController(ContactService contactService, RateLimiterService rateLimiterService) {
        this.contactService = contactService;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping(value = "/create", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createContact(@RequestBody @Valid CreateContactRequest createContactRequest) {
        if (!rateLimiterService.tryConsume())
            return ResponseEntity.status(TOO_MANY_REQUESTS).body("Rate limit exceeded");

        contactService.createContact(createContactRequest);
        return ResponseEntity.status(CREATED).build();
    }

}