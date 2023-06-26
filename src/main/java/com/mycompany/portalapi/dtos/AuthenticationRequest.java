package com.mycompany.portalapi.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AuthenticationRequest(
        @NotNull(message = "email must not be null")
        @NotEmpty(message = "email must not be empty")
        String email,
        @NotNull(message = "password must not be null")
        @NotEmpty(message = "password must not be empty")
        String password
) {
}
