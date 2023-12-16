package com.mycompany.portalapi.services;

import com.mycompany.portalapi.dtos.DepartmentDTO;
import com.mycompany.portalapi.exceptions.IllegalArgumentException;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.faculty.Department;
import com.mycompany.portalapi.models.faculty.Faculty;
import com.mycompany.portalapi.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final FacultyService facultyService;

    public void addDepartment(DepartmentDTO departmentDTO) {
        filterDepartmentDTO(departmentDTO);
        Faculty faculty = facultyService.getById(departmentDTO.facultyId());
        Department department = Department.builder()
                .departmentName(departmentDTO.departmentName())
                .description(departmentDTO.description())
                .faculty(faculty)
                .build();
        departmentRepository.save(department);
    }
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("wrong department id"));
    }
    public Department getDepartmentByName(String name) {
        return departmentRepository.findByDepartmentName(name).orElseThrow(() -> new IllegalArgumentException("wrong department name"));
    }


    public Department getDepartmentByDepartmentIdAndFieldStudyId(long depId, long facultyId) {
        log.info("department not found with provided field id: {} and department id: {}", facultyId, depId);
        Faculty f = facultyService.getById(facultyId);
        return departmentRepository.findDepartmentByIdAndFaculty(depId, f)
                .orElseThrow(() -> new ResourceNotFoundException("department not found with provided fieldStudyId: " + facultyId + " and departmentId: " + depId));
    }

    public List<Department> getAllDepartmentByFieldStudyId(Long facultyId) {
        Faculty f = facultyService.getById(facultyId);
        return departmentRepository.findAllByFaculty(f);
    }


    public void filterDepartmentDTO(DepartmentDTO departmentDTO){
        if(departmentDTO == null)
            throw new IllegalArgumentException("Department should not be null");
        if(departmentDTO.departmentName() == null || departmentDTO.departmentName().isEmpty()){
            throw new IllegalArgumentException("Department \"name\" should not be empty");
        }
        if(departmentDTO.facultyId() == null){
            throw new IllegalArgumentException("Department \"fieldOfStudyId\" should not be empty");
        }
        if(departmentDTO.description() == null || departmentDTO.description().isEmpty()){
            throw new IllegalArgumentException("Department \"description\" should not be empty");
        }
    }
}

