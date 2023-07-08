package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.models.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserApp, Long> {
    Optional<UserApp> findByEmail(String email);
    boolean existsByEmail(String email);
}
