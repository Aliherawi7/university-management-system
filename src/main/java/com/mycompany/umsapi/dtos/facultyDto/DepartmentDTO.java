package com.mycompany.umsapi.dtos.facultyDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DepartmentDTO(
        @NotNull(message = "department name shouldn't be null")
        @NotEmpty(message = "department name shouldn't be empty")
        String departmentName,
        @NotNull(message = "department description shouldn't be null")
        @NotEmpty(message = "department description shouldn't be empty")
        String description,
        int semesterNumber) {
}
