package com.mycompany.portalapi.dtos;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record IdentificationDTO(
        @NonNull
        Long nationalId,
        @NonNull
         Long caseNumber,
        @NonNull
         Long pageNumber,
        @NonNull
         Long registrationNumber
) {
}
