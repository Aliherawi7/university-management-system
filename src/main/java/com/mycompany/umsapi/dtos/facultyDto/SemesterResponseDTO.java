package com.mycompany.umsapi.dtos.facultyDto;

import lombok.Builder;

import java.util.Set;

@Builder
public record SemesterResponseDTO(
        Long semesterId,
        Long departmentId,
        Integer semesterNumber,
        Integer subjectTotal,
        Double paymentAmount,
        Integer Credit,
        Set<SubjectResponseDTO> subjects

) {
}
