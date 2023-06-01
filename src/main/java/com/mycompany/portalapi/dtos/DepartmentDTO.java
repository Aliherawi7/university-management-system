package com.mycompany.portalapi.dtos;

import lombok.Builder;

@Builder
public record DepartmentDTO(Long fieldOfStudyId,
                            String departmentName,
                            String description) {
}
