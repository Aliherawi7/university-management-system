package com.mycompany.portalapi.services;

import com.mycompany.portalapi.dtos.DepartmentDTO;
import com.mycompany.portalapi.dtos.FieldOfStudyDTO;
import com.mycompany.portalapi.exceptions.IllegalArgumentException;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.Department;
import com.mycompany.portalapi.repositories.DepartmentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public void addDepartment(DepartmentDTO departmentDTO) {
        filterDepartmentDTO(departmentDTO);
        Department department = Department.builder()
                .departmentName(departmentDTO.departmentName())
                .description(departmentDTO.description())
                .fieldOfStudyId(departmentDTO.fieldOfStudyId())
                .build();
        departmentRepository.save(department);
    }

    public Department getDepartmentByDepartmentIdAndFieldStudyId(long depId, long fieldStudyId) {
        log.info("department not found with provided field id: {} and department id: {}", fieldStudyId, depId);
        return departmentRepository.findDepartmentByIdAndFieldOfStudyId(depId, fieldStudyId)
                .orElseThrow(() -> new ResourceNotFoundException("department not found with provided fieldStudyId: " + fieldStudyId + " and departmentId: " + depId));
    }

    public List<Department> getAllDepartmentByFieldStudyId(Long fieldStudyId) {
        return departmentRepository.findAllByFieldOfStudyId(fieldStudyId);
    }


    public void filterDepartmentDTO(DepartmentDTO departmentDTO){
        if(departmentDTO == null)
            throw new IllegalArgumentException("Department should not be null");
        if(departmentDTO.departmentName() == null || departmentDTO.departmentName().isEmpty()){
            throw new IllegalArgumentException("Department \"name\" should not be empty");
        }
        if(departmentDTO.fieldOfStudyId() == null){
            throw new IllegalArgumentException("Department \"fieldOfStudyId\" should not be empty");
        }
        if(departmentDTO.description() == null || departmentDTO.description().isEmpty()){
            throw new IllegalArgumentException("Department \"description\" should not be empty");
        }
    }
}

