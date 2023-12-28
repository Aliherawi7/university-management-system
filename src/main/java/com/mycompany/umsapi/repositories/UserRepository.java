package com.mycompany.umsapi.repositories;

import com.mycompany.umsapi.models.hrms.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserApp, Long> {
    Optional<UserApp> findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteUserAppByEmail(String email);
}
