package com.mycompany.portalapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UpdateUserDTO(
        @NotNull(message = "ایمیل نباید خالی باشد")
        @Email(message = "ایمیل نامعتبر است")
        String email,
        @NotNull(message = "رمز عبور قبلی نباید خالی باشد")
        String previousPassword,
        @NotNull(message = "رمز عبور جدید نباید خالی باشد")
        String newPassword

) {
}
