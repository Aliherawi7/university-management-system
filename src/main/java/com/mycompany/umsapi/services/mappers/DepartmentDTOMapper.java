package com.mycompany.umsapi.services.mappers;

import com.mycompany.umsapi.dtos.facultyDto.DepartmentResponseDTO;
import com.mycompany.umsapi.models.faculty.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DepartmentDTOMapper implements Function<Department, DepartmentResponseDTO> {
    private final SemesterResponseDTOMapper semesterResponseDTOMapper;
    @Override
    public DepartmentResponseDTO apply(Department department) {
        return DepartmentResponseDTO.builder()
                .departmentId(department.getId())
                .facultyId(department.getFaculty().getId())
                .departmentName(department.getDepartmentName())
                .departmentDescription(department.getDescription())
                .semesters(department.getSemesters().stream().map(semesterResponseDTOMapper)
                        .collect(Collectors.toSet()))
                .build();
    }
}
