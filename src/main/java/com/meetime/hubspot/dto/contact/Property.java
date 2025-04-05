package com.meetime.hubspot.dto.contact;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Property {

    @NotBlank(message = "email address is required to avoid duplicate contacts in HubSpot")
    private String email;

    private String firstName;
    private String lastName;
    private String phone;
    private String company;
    private String website;
    private String lifecyclestage;

}