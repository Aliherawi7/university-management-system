package com.mycompany.umsapi.repositories;

import com.mycompany.umsapi.models.faculty.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

    Optional<Semester> findBySemesterAndDepartment_Id(Long semester, Long department);
    Set<Semester> findAllByDepartmentId(Long departmentId);
}
