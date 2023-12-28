package com.mycompany.umsapi.dtos.facultyDto;

import lombok.Builder;

@Builder
public record SemesterDTO(Long departmentId,
                          String semesterName,
                          Integer semesterNumber,
                          String description
                          ) {
}
