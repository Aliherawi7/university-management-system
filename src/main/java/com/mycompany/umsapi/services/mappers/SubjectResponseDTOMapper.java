package com.mycompany.umsapi.services.mappers;

import com.mycompany.umsapi.dtos.facultyDto.SubjectResponseDTO;
import com.mycompany.umsapi.models.faculty.Subject;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SubjectResponseDTOMapper implements Function<Subject, SubjectResponseDTO> {
    @Override
    public SubjectResponseDTO apply(Subject subject) {
        return SubjectResponseDTO.builder()
                .subjectId(subject.getId())
                .credit(subject.getCredit())
                .name(subject.getName())
                .build();
    }
}
