package com.mycompany.umsapi.services.mappers;

import com.mycompany.umsapi.dtos.facultyDto.FacultyResponseDTO;
import com.mycompany.umsapi.models.faculty.Faculty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FacultyResponseDTOMapper implements Function<Faculty, FacultyResponseDTO> {
    private final DepartmentDTOMapper departmentDTOMapper;
    @Override
    public FacultyResponseDTO apply(Faculty faculty) {
        return FacultyResponseDTO
                .builder()
                .id(faculty.getId())
                .facultyName(faculty.getFacultyName())
                .description(faculty.getDescription())
                .departments(faculty.getDepartments().stream().map(departmentDTOMapper).collect(Collectors.toSet()))
                .build();
    }
}
