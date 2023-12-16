package com.mycompany.portalapi.dtos.postDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record PostRequestDTO(

        String fieldOfStudy,
        String department,
        String message,
        @NotNull(message = "آی دی نویسنده نباید خالی باشد")
        String authorEmail,
        Integer semester,
        @NotNull(message = "isPublic must not be null")
        @NotEmpty(message = "isPublic must not be empty")
        Boolean isPublic
) {
}
