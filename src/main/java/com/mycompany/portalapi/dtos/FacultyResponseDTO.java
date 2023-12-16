package com.mycompany.portalapi.dtos;

import lombok.Builder;

@Builder
public record FacultyResponseDTO(Long id, String facultyName, String description) {
}
