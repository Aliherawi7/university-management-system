package com.mycompany.umsapi.services.mappers;

import com.mycompany.umsapi.dtos.facultyDto.SemesterResponseDTO;
import com.mycompany.umsapi.models.faculty.Semester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SemesterResponseDTOMapper implements Function<Semester, SemesterResponseDTO> {
    private final SubjectResponseDTOMapper subjectResponseDTOMapper;
    @Override
    public SemesterResponseDTO apply(Semester semester) {
        return SemesterResponseDTO.builder()
                .semesterId(semester.getId())
                .departmentId(semester.getDepartment().getId())
                .semesterNumber(semester.getSemester())
                .Credit(semester.getCredit())
                .paymentAmount(semester.getPaymentAmount())
                .subjectTotal(semester.getSubjectTotal())
                .subjects(semester.getSubjects().stream().map(subjectResponseDTOMapper)
                        .collect(Collectors.toSet()))
                .build();
    }
}
