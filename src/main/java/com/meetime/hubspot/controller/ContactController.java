package com.meetime.hubspot.controller;

import com.meetime.hubspot.domain.contact.CreateContactRequest;
import com.meetime.hubspot.service.ContactService;
import io.github.bucket4j.Bucket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "API responsible for creating a contact in the CRM with Rate Limit policies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contact created successfully", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "429", description = "Rate limit exceeded", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/create", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "API responsible for creating a contact in the CRM with Rate Limit policies.")
    public ResponseEntity<?> createContact(@RequestBody @Valid CreateContactRequest createContactRequest) {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }

        contactService.createContact(createContactRequest);
        return ResponseEntity.status(CREATED).build();
    }

}