package com.meetime.hubspot.domain.contact;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.meetime.hubspot.enums.AssociationCategoryEnum;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Type(AssociationCategoryEnum associationCategory, Long associationTypeId) {

}