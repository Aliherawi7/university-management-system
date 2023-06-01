package com.mycompany.portalapi.dtos;

import lombok.Builder;

@Builder
public record FieldOfStudyDTO(String fieldName,String description) {
}
