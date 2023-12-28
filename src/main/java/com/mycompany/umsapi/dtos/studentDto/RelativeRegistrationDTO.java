package com.mycompany.umsapi.dtos.studentDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RelativeRegistrationDTO(
        @NotBlank(message = "نام اقارب محصل نباید خالی باشد")
        String name,
        @NotBlank(message = "وظیفه اقارب محصل نباید خالی باشد")
        String job,
        @NotBlank(message = "شماره تماس اقارب محصل نباید خالی باشد")
        String phoneNumber,
        @NotBlank(message = "موقیعت وظیفه اقارب محصل نباید خالی باشد")
        String jobLocation,
        @NotNull(message = "نسبت اقارب محصل نباید خالی باشد")
        Integer relationshipId
) {
}
