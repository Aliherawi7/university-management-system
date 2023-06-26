package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.constants.GenderName;
import com.mycompany.portalapi.models.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenderRepository extends JpaRepository<Gender, Integer> {
    Optional<Gender> findByName(String name);
}
