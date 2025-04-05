package com.meetime.hubspot.domain.contact;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Property(@NotBlank(message = "email address is required to avoid duplicate contacts in HubSpot")
                       String email,
                       String firstName,
                       String lastName,
                       String phone,
                       String company,
                       String website,
                       String lifecyclestage) {

}