package com.mycompany.portalapi.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mycompany.portalapi.config.RelationDeserializer;
import com.mycompany.portalapi.constants.Relation;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record RelativeRegistrationDTO(
        @NonNull
        String name,
        @NonNull
        String job,
        @NonNull
        String phoneNumber,
        @NonNull
        String jobLocation,
        @NonNull
        @JsonDeserialize(using = RelationDeserializer.class)
        Relation relation
) {
}
