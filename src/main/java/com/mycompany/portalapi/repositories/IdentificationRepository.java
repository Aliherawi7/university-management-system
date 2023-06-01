package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.Identification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentificationRepository extends JpaRepository<Identification, Long> {
    Identification findByStudentId(Long userId);
}
