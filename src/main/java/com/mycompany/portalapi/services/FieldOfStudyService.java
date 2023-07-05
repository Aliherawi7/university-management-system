package com.mycompany.portalapi.services;

import com.mycompany.portalapi.dtos.FieldOfStudyDTO;
import com.mycompany.portalapi.exceptions.IllegalArgumentException;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.FieldOfStudy;
import com.mycompany.portalapi.repositories.FieldOfStudyRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FieldOfStudyService {
    private final FieldOfStudyRepository fieldOfStudyRepository;

    /* save the field of study in DB */
    public void addFieldOfStudy(FieldOfStudyDTO fieldOfStudyDTO) {
        filterFieldOfStudyDTO(fieldOfStudyDTO);
        FieldOfStudy fieldOfStudy = FieldOfStudy
                .builder()
                .fieldName(fieldOfStudyDTO.fieldName())
                .description(fieldOfStudyDTO.description())
                .build();
        fieldOfStudyRepository.save(fieldOfStudy);
    }

    public FieldOfStudy getById(long id) {
        log.info("field-of-study fetch with id = {}", id);
        return fieldOfStudyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("field-of-study not found with id: " + id));
    }

    public void filterFieldOfStudyDTO(FieldOfStudyDTO fieldOfStudyDTO){
        if(fieldOfStudyDTO == null)
            throw new IllegalArgumentException("Field of study should not be null");
        if(fieldOfStudyDTO.fieldName() == null || fieldOfStudyDTO.fieldName().isEmpty()){
            throw new IllegalArgumentException("Field of study \"name\" should not be empty");
        }
        if(fieldOfStudyDTO.description() == null || fieldOfStudyDTO.description().isEmpty()){
            throw new IllegalArgumentException("Field of study \"description\" should not be empty");
        }
    }

}
