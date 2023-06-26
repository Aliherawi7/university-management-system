package com.mycompany.portalapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
public record LocationDTO(
        @NotNull(message = "villageOrQuarter must not be null")
        @NotBlank(message = "villageOrQuarter must not be empty")
        String villageOrQuarter,
        @NotNull(message = "district must not be null")
        @NotBlank(message = "district must not be empty")
        String district,
        @NotNull(message = "city must not be null")
        @NotBlank(message = "city must not be empty")
        String city,
        @NotNull(message = "city must not be empty")
        Boolean current) {
}
