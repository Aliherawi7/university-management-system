package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Identification;
import com.mycompany.portalapi.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentificationRepository extends JpaRepository<Identification, Long> {
    Identification findByStudentId(Long userId);
    boolean existsByNationalId(Long studentIdentification);

}
