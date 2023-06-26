package com.mycompany.portalapi.repositories;


import com.mycompany.portalapi.models.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaritalStatusRepository extends JpaRepository<MaritalStatus, Integer> {
    Optional<MaritalStatus> findByName(String name);
}
