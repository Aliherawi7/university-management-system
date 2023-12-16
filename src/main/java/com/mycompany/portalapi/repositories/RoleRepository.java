package com.mycompany.portalapi.repositories;

import com.mycompany.portalapi.constants.RoleName;
import com.mycompany.portalapi.models.hrms.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleName roleName);
    boolean existsByRoleName(RoleName roleName);
}
