package com.mycompany.umsapi.dtos.facultyDto;

import lombok.Builder;

import java.util.Set;

@Builder
public record FacultyResponseDTO(Long id,
                                 String facultyName,
                                 String description,
                                 Set<DepartmentResponseDTO> departments) {
}
