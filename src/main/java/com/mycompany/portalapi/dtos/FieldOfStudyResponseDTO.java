package com.mycompany.portalapi.dtos;

import lombok.Builder;

@Builder
public record FieldOfStudyResponseDTO(Long id, String fieldName,String description) {
}
