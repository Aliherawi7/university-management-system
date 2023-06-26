package com.mycompany.portalapi.dtos;

import com.mycompany.portalapi.constants.RelationName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RelativeRegistrationDTO(
        @NotNull(message = "the relation's name must not be null")
        @NotBlank(message = "the relation's name must not be empty")
        String name,
        @NotNull(message = "the relation's job must not be null")
        @NotBlank(message = "the relation's job must not be empty")
        String job,
        @NotNull(message = "the relation's phoneNumber must not be null")
        @NotBlank(message = "the relation's phoneNumber must not be empty")
        String phoneNumber,
        @NotNull(message = "the relation's jobLocation must not be null")
        @NotBlank(message = "the relation's jobLocation must not be empty")
        String jobLocation,
        @NotNull(message = "relationship must not be null")
        @NotBlank(message = "relationship must not be empty")
        String relationship
) {
}
