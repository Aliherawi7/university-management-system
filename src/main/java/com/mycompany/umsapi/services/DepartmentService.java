package com.mycompany.umsapi.services;

import com.mycompany.umsapi.dtos.APIResponse;
import com.mycompany.umsapi.dtos.facultyDto.DepartmentDTO;
import com.mycompany.umsapi.dtos.facultyDto.DepartmentResponseDTO;
import com.mycompany.umsapi.exceptions.IllegalArgumentException;
import com.mycompany.umsapi.exceptions.ResourceNotFoundException;
import com.mycompany.umsapi.models.faculty.Department;
import com.mycompany.umsapi.models.faculty.Faculty;
import com.mycompany.umsapi.models.faculty.Semester;
import com.mycompany.umsapi.repositories.DepartmentRepository;
import com.mycompany.umsapi.services.mappers.DepartmentDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final FacultyService facultyService;
    private final DepartmentDTOMapper departmentDTOMapper;
    private final RequestObjectValidatorService<DepartmentDTO> departmentDTORequestValidatorService;

    public APIResponse addDepartmentForController(DepartmentDTO departmentDTO, Long facultyId) {
        addDepartment(departmentDTO, facultyId);
        return APIResponse.builder()
                .message("department successfully added!")
                .statusCode(HttpStatus.OK.value())
                .zonedDateTime(ZonedDateTime.now())
                .httpStatus(HttpStatus.OK)
                .build();
    }
    public void addDepartment(DepartmentDTO departmentDTO, Long facultyId) {
        departmentDTORequestValidatorService.validate(departmentDTO);
        Faculty faculty = facultyService.getById(facultyId);
        Department department = Department.builder()
                .departmentName(departmentDTO.departmentName())
                .faculty(faculty)
                .description(departmentDTO.description())
                .build();
        department = departmentRepository.save(department);
        faculty.getDepartments().add(department);
        facultyService.addFaculty(faculty);
    }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("wrong department id"));
    }
    public Department getDepartmentByName(String name) {
        return departmentRepository.findByDepartmentName(name).orElseThrow(() -> new IllegalArgumentException("wrong department name"));
    }


    public DepartmentResponseDTO getDepartmentByDepartmentIdAndFacultyId(long depId, long facultyId) {
        log.info("department not found with provided field id: {} and department id: {}", facultyId, depId);
        Faculty f = facultyService.getById(facultyId);
        Department d = departmentRepository.findDepartmentByIdAndFaculty(depId, f)
                .orElseThrow(
                        () -> new ResourceNotFoundException
                                ("department not found with provided fieldStudyId: " + facultyId + " and departmentId: " + depId));
        return departmentDTOMapper.apply(d);
    }

    public Set<DepartmentResponseDTO> getAllDepartmentByFacultyId(Long facultyId) {
        Faculty f = facultyService.getById(facultyId);
        return departmentRepository.findAllByFaculty(f).stream().map(departmentDTOMapper).collect(Collectors.toSet());
    }

    public Semester getSemesterByIdAndDepartmentId(Integer semester, Long departmentId){
        return getDepartmentById(departmentId).getSemesters().stream()
                .filter(item -> Objects.equals(item.getSemester(), semester))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid semester number!"));
    }
    public Semester getSemesterByIdAndDepartment(Integer semester, Department department){
        return department.getSemesters().stream()
                .filter(item -> Objects.equals(item.getSemester(), semester))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid semester number!"));
    }




    public APIResponse deleteFacultyIdAndDepartmentById(Long facultyId, Long departmentId) {
        if(!departmentRepository.existsById(departmentId)){
            throw new ResourceNotFoundException("department not found with provided id: "+departmentId);
        }
        departmentRepository.deleteById(departmentId);
        return APIResponse.builder()
                .zonedDateTime(ZonedDateTime.now())
                .message("Department deleted successfully with id: "+departmentId)
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .build();
    }
}

