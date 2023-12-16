package com.mycompany.portalapi.services;

import com.mycompany.portalapi.dtos.FieldOfStudyDTO;
import com.mycompany.portalapi.dtos.FacultyResponseDTO;
import com.mycompany.portalapi.exceptions.IllegalArgumentException;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.faculty.Faculty;
import com.mycompany.portalapi.repositories.FieldOfStudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacultyService {
    private final FieldOfStudyRepository fieldOfStudyRepository;

    /* save the field of study in DB */
    public void addFieldOfStudy(FieldOfStudyDTO fieldOfStudyDTO) {
        filterFieldOfStudyDTO(fieldOfStudyDTO);
        Faculty faculty = Faculty
                .builder()
                .facultyName(fieldOfStudyDTO.fieldName())
                .description(fieldOfStudyDTO.description())
                .build();
        fieldOfStudyRepository.save(faculty);
    }

    public Page<FacultyResponseDTO> getAllFieldOfStudies(){
        return fieldOfStudyRepository.findAll(PageRequest.of(0,(int)fieldOfStudyRepository.count())).map(item ->
                FacultyResponseDTO.builder().id(item.getId()).facultyName(item.getFacultyName()).description(item.getDescription()).build());
    }

    public Faculty getById(long id) {
        log.info("field-of-study fetch with id = {}", id);
        return fieldOfStudyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("field-of-study not found with id: " + id));
    }

    public Faculty getByName(String facultyName) {
        log.info("faculty fetch with name = {}", facultyName);
        return fieldOfStudyRepository.findByFacultyName(facultyName)
                .orElseThrow(() -> new ResourceNotFoundException("faculty not found with provided name: " + facultyName));
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
