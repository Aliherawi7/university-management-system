package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

    Optional<Semester> findBySemesterAndFieldOfStudyAndDepartment(Long semester, String fieldOfStudy, String department);
}
