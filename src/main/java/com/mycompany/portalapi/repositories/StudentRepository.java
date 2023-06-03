package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Location;
import com.mycompany.portalapi.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);
}
