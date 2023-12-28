package com.mycompany.umsapi.repositories;

import com.mycompany.umsapi.models.hrms.Identification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentificationRepository extends JpaRepository<Identification, Long> {
    boolean existsByNationalId(Long studentIdentification);

}
