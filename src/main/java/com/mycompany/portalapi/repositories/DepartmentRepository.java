package com.mycompany.portalapi.repositories;


import com.mycompany.portalapi.models.faculty.Department;
import com.mycompany.portalapi.models.faculty.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findDepartmentByIdAndFaculty(Long departmentId, Faculty facultyId);
    List<Department> findAllByFaculty(Faculty faculty);
    Optional<Department> findByDepartmentName(String name);
}
