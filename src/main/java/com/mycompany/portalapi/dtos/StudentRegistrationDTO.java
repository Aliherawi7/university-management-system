package com.mycompany.portalapi.dtos;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record StudentRegistrationDTO(
        @NonNull
        StudentPersonalInfo studentPersonalInfo,
        @NonNull
        IdentificationDTO identification,
        @NonNull
        Locations locations,
        @NonNull
        List<RelativeRegistrationDTO> relatives,
        String role
) {
}


