package com.mycompany.umsapi.dtos;

import com.mycompany.umsapi.models.hrms.UserType;
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
        UserType userType,
        List<String> roles
) {
}
