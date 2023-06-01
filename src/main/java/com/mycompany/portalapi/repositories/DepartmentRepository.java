package com.mycompany.portalapi.repositories;


import com.mycompany.portalapi.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findDepartmentByIdAndFieldOfStudyId(Long departmentId, Long fieldOfStudyId);
    List<Department> findAllByFieldOfStudyId(Long fieldStudyId);
}
