package com.mycompany.umsapi.dtos.users;

import com.mycompany.umsapi.models.hrms.Role;
import com.mycompany.umsapi.models.hrms.UserType;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserRolesDTO(
        Long userId,
        String email,
        UserType userType,
        Set<Role> roles
) {
}
