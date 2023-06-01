package com.mycompany.portalapi.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mycompany.portalapi.config.RelationDeserializer;
import com.mycompany.portalapi.constants.Relation;
import lombok.Builder;

@Builder
public record RelativeRegistrationDTO(
        String name,
        String job,
        String phoneNumber,
        String jobLocation,
        @JsonDeserialize(using = RelationDeserializer.class)
        Relation relation
) {
}
