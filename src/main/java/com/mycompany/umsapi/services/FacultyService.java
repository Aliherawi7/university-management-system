package com.mycompany.umsapi.services;

import com.mycompany.umsapi.dtos.*;
import com.mycompany.umsapi.dtos.facultyDto.DepartmentResponseDTO;
import com.mycompany.umsapi.dtos.facultyDto.FacultyDTO;
import com.mycompany.umsapi.dtos.facultyDto.FacultyResponseDTO;
import com.mycompany.umsapi.exceptions.IllegalArgumentException;
import com.mycompany.umsapi.exceptions.ResourceNotFoundException;
import com.mycompany.umsapi.models.faculty.Faculty;
import com.mycompany.umsapi.repositories.FacultyRepository;
import com.mycompany.umsapi.services.mappers.DepartmentDTOMapper;
import com.mycompany.umsapi.services.mappers.FacultyResponseDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final DepartmentDTOMapper departmentDTOMapper;
    private final FacultyResponseDTOMapper facultyResponseDTOMapper;

    /* save the field of study in DB */
    public void addFaculty(FacultyDTO facultyDTO) {
        if(facultyDTO == null){
            throw new java.lang.IllegalArgumentException("faculty should not be empty or null!");
        }
        filterFacultyDTO(facultyDTO);
        Faculty faculty = Faculty
                .builder()
                .facultyName(facultyDTO.fieldName())
                .description(facultyDTO.description())
                .build();
        facultyRepository.save(faculty);
    }
    public void addFaculty(Faculty faculty) {
        if(faculty == null){
            throw new java.lang.IllegalArgumentException("faculty should not be empty or null!");
        }
        facultyRepository.save(faculty);
    }

    public Page<FacultyResponseDTO> getAllFaculties(){
        return facultyRepository.findAll(PageRequest.of(0,(int) facultyRepository.count())).map(item ->{
                    Set<DepartmentResponseDTO> departments = item.getDepartments().stream().map(departmentDTOMapper).collect(Collectors.toSet());
                    return FacultyResponseDTO
                            .builder()
                            .id(item.getId())
                            .facultyName(item.getFacultyName())
                            .description(item.getDescription())
                            .departments(departments)
                            .build();
                });
    }

    public FacultyResponseDTO getFacultyResponseDTOById(long id) {
        log.info("faculty fetch with id = {}", id);
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with provided id: " + id));
        return facultyResponseDTOMapper.apply(faculty);
    }
    public Faculty getById(long id) {
        log.info("faculty fetch with id = {}", id);
        return facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with provided id: " + id));

    }

    public Faculty getByName(String facultyName) {
        return facultyRepository.findByFacultyName(facultyName)
                .orElseThrow(() -> new ResourceNotFoundException("faculty not found with provided name: " + facultyName));
    }

    public void filterFacultyDTO(FacultyDTO facultyDTO){
        if(facultyDTO == null)
            throw new IllegalArgumentException("Faculty should not be null");
        if(facultyDTO.fieldName() == null || facultyDTO.fieldName().isEmpty()){
            throw new IllegalArgumentException("Faculty \"name\" should not be empty");
        }
        if(facultyDTO.description() == null || facultyDTO.description().isEmpty()){
            throw new IllegalArgumentException("Faculty \"description\" should not be empty");
        }
    }

    public APIResponse deleteFacultyById(Long facultyId){
        if(!facultyRepository.existsById(facultyId)){
            throw new ResourceNotFoundException("faculty not found with provided id: "+ facultyId);
        }
        facultyRepository.deleteById(facultyId);
        return APIResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("faculty successfully delete with id: "+facultyId)
                .statusCode(HttpStatus.OK.value())
                .zonedDateTime(ZonedDateTime.now())
                .build();
    }

}
