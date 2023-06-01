package com.mycompany.portalapi.dtos;

import lombok.Builder;

@Builder
public record IdentificationDTO(
         Long nationalId,
         Long caseNumber,
         Long pageNumber,
         Long registrationNumber
) {
}
