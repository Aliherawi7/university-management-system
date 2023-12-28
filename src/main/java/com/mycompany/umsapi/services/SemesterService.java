package com.mycompany.umsapi.services;

import com.mycompany.umsapi.dtos.APIResponse;
import com.mycompany.umsapi.dtos.facultyDto.SemesterDTO;
import com.mycompany.umsapi.dtos.facultyDto.SemesterRegistrationDTO;
import com.mycompany.umsapi.dtos.facultyDto.SemesterResponseDTO;
import com.mycompany.umsapi.exceptions.IllegalArgumentException;
import com.mycompany.umsapi.exceptions.ResourceNotFoundException;
import com.mycompany.umsapi.models.faculty.Department;
import com.mycompany.umsapi.models.faculty.Semester;
import com.mycompany.umsapi.repositories.DepartmentRepository;
import com.mycompany.umsapi.repositories.SemesterRepository;
import com.mycompany.umsapi.services.mappers.SemesterResponseDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SemesterService {
    private final SemesterRepository semesterRepository;
    private final DepartmentRepository departmentRepository;
    private final SemesterResponseDTOMapper semesterResponseDTOMapper;


    public APIResponse addSemester(SemesterRegistrationDTO semesterRegistrationDTO, Long facultyId, Long departmentId){
        // do check
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("invalid department id"));
        Semester semester = Semester.builder()
                .department(department)
                .semester(semesterRegistrationDTO.semester())
                .credit(semesterRegistrationDTO.credit())
                .subjectTotal(semesterRegistrationDTO.subjectTotal())
                .paymentAmount(semesterRegistrationDTO.paymentAmount())
                .subjects(semesterRegistrationDTO.subjects())
                .build();
        semester = semesterRepository.save(semester);
        return APIResponse.builder()
                .message("semester successfully added with id: "+semester.getId())
                .httpStatus(HttpStatus.OK)
                .zonedDateTime(ZonedDateTime.now())
                .statusCode(HttpStatus.OK.value())
                .build();
    }
    public void addSemester(SemesterDTO semesterDTO){
        // do check
        Department department = departmentRepository.findById(semesterDTO.departmentId())
                        .orElseThrow(() -> new IllegalArgumentException("invalid department id"));
        Semester semester = Semester.builder()
                .department(department)
                .semester(semesterDTO.semesterNumber())
                .build();
        semesterRepository.save(semester);
    }

    public Set<SemesterResponseDTO> getSemestersByFacultyIdAndDepartmentId(Long facultyId, Long department){
        return semesterRepository
                .findAllByDepartmentId(department)
                .stream()
                .map(semesterResponseDTOMapper)
                .collect(Collectors.toSet());
    }
    public Semester getSemesterById(Long semesterId){
        return semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("semester not found with provided id: "+semesterId));
    }

    public void updateSemester(Semester semester){
        semesterRepository.save(semester);
    }

    public void deleteSemester(Long id){
        semesterRepository.deleteById(id);
    }

}
