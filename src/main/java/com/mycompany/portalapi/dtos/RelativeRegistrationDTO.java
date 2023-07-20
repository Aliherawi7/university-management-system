package com.mycompany.portalapi.dtos;

import com.mycompany.portalapi.constants.RelationName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
        @NotBlank(message = "موقیعت وطیفه اقارب محصل نباید خالی باشد")
        String jobLocation,
        @NotBlank(message = "نسبت اقارب محصل نباید خالی باشد")
        String relationship
) {
}
