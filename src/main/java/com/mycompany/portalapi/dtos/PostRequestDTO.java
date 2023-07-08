package com.mycompany.portalapi.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PostRequestDTO(

        String fieldOfStudy,

        String department,
        @NotNull(message = "fieldOfStudy must not be null")
        String message,
        @NotNull(message = "fieldOfStudy must not be null")
        Long authorId,
        Integer semester,
        @NotNull(message = "fieldOfStudy must not be null")
        @NotEmpty(message = "isPublic must not be empty")
        Boolean isPublic
) {
}
