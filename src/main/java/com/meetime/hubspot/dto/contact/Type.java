package com.meetime.hubspot.dto.contact;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.meetime.hubspot.enums.AssociationCategoryEnum;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Type {

    private AssociationCategoryEnum associationCategory;
    private Long associationTypeId;

}