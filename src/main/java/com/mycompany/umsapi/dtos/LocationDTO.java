package com.mycompany.umsapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LocationDTO(
        @NotBlank(message = "قریه یا گذر نباید خالی باشد")
        String villageOrQuarter,
        @NotBlank(message = "ناحیه یا ولسوالی نباید خالی باشد")
        String district,
        @NotBlank(message = "شهر نباید خالی باشد")
        String city,
        @NotNull(message = "موقعیت فعلی نباید خالی باشد")
        Boolean current) {
}
