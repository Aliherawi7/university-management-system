package com.mycompany.umsapi.dtos.facultyDto;

import lombok.Builder;

@Builder
public record SubjectResponseDTO(
        Long subjectId,
        String name,
        Integer semester,
        Integer credit

) {
}
