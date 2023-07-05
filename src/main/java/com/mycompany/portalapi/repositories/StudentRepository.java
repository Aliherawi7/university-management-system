package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Location;
import com.mycompany.portalapi.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);

    Page<Student> findAllByName(String name, Pageable pageable);
    Page<Student> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
}
