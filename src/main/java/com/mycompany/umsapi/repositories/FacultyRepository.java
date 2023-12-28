package com.mycompany.umsapi.repositories;


import com.mycompany.umsapi.models.faculty.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByFacultyName(String name);
}
