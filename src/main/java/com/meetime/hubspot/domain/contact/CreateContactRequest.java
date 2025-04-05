package com.meetime.hubspot.domain.contact;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateContactRequest(
        @NotNull(message = "at least one property is required to create a contact in HubSpot") Property properties,
        List<Association> associations) {

}