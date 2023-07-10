package com.mycompany.portalapi.dtos;

import com.mycompany.portalapi.constants.RelationName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RelativeRegistrationDTO(
        @NotNull(message = "نام اقارب محصل نباید خالی باشد")
        String name,
        @NotNull(message = "وظیفه اقارب محصل نباید خالی باشد")
        String job,
        @NotNull(message = "شماره تماس اقارب محصل نباید خالی باشد")
        String phoneNumber,
        @NotNull(message = "موقیعت وطیفه اقارب محصل نباید خالی باشد")
        String jobLocation,
        @NotNull(message = "نسبت اقارب محصل نباید خالی باشد")
        String relationship
) {
}
