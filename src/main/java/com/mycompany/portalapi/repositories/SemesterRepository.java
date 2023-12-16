package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.faculty.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

    Optional<Semester> findBySemesterAndDepartment_Id(Long semester, Long department);
}
