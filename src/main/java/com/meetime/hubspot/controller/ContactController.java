package com.meetime.hubspot.controller;

import com.meetime.hubspot.dto.contact.CreateContactRequest;
import com.meetime.hubspot.service.ContactService;
import io.github.bucket4j.Bucket;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;
    private final Bucket bucket;

    @PostMapping(value = "/create", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createContact(@RequestBody @Valid CreateContactRequest createContactRequest) {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }

        contactService.createContact(createContactRequest);
        return ResponseEntity.status(CREATED).build();
    }

}