package com.mycompany.portalapi.dtos;

import lombok.Builder;

@Builder
public record DepartmentDTO(Long facultyId,
                            String departmentName,
                            String description,
                            int semesterNumber) {
}
