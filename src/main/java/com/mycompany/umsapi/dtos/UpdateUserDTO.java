package com.mycompany.umsapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UpdateUserDTO(
        @NotNull(message = "آی دی نمبر نباید خالی باشد")
        Long userId,
        @NotNull(message = "ایمیل نباید خالی باشد")
        @Email(message = "ایمیل نامعتبر است")
        String email,

        String previousPassword,
        @NotNull(message = "رمز عبور جدید نباید خالی باشد")
        String newPassword

) {
}
