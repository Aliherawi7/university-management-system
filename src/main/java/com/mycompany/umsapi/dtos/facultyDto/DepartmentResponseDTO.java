package com.mycompany.umsapi.dtos.facultyDto;

import lombok.Builder;

import java.util.Set;

@Builder
public record DepartmentResponseDTO(
        Long facultyId,
        Long departmentId,
        String departmentName,
        String departmentDescription,
        Set<SemesterResponseDTO> semesters

) {
}
