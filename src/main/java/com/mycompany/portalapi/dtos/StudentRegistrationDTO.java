package com.mycompany.portalapi.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record StudentRegistrationDTO(
        @Valid
        StudentPersonalInfo studentPersonalInfo,
        @Valid
        IdentificationDTO identification,
        @Valid
        Locations locations,
        @Valid
        List<RelativeRegistrationDTO> relatives,
        String role
) {
}


