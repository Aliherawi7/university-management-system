package com.mycompany.portalapi.repositories;


import com.mycompany.portalapi.models.faculty.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FieldOfStudyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByFacultyName(String name);
}
