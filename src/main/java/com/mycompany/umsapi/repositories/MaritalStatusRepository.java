package com.mycompany.umsapi.repositories;


import com.mycompany.umsapi.models.hrms.MaritalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaritalStatusRepository extends JpaRepository<MaritalStatus, Integer> {
    Optional<MaritalStatus> findByName(String name);
}
