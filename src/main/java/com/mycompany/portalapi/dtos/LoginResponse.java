package com.mycompany.portalapi.dtos;

import com.mycompany.portalapi.models.Role;
import lombok.Builder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Builder
public record LoginResponse(
        String token,
        String name,
        String lastname,
        String email,
        String imageUrl,
        Long userId,
        List<String> roles
) {
}
