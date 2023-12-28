package com.mycompany.umsapi.repositories;

import com.mycompany.umsapi.constants.RoleName;
import com.mycompany.umsapi.models.hrms.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleName roleName);
    boolean existsByRoleName(RoleName roleName);
}
