package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.hrms.Identification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentificationRepository extends JpaRepository<Identification, Long> {
    boolean existsByNationalId(Long studentIdentification);

}
