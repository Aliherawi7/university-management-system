package com.mycompany.portalapi.dtos;

import lombok.Builder;
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
