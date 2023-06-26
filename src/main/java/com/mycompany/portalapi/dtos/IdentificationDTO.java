package com.mycompany.portalapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record IdentificationDTO(

        @NotNull(message = "nationalId not be null")
        @Positive(message = "nationalId must be greater than zero")
        Long nationalId,
        @NotNull(message = "caseNumber not be null")
        @Positive(message = "caseNumber must be greater than zero")
        Long caseNumber,
        @NotNull(message = "pageNumber not be null")
        @Positive(message = "pageNumber must be greater than zero")
        Long pageNumber,
        @NotNull(message = "registrationNumber not be null")
        @Positive(message = "registrationNumber must be greater than zero")
        Long registrationNumber
) {
}
