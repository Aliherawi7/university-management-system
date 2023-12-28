package com.mycompany.umsapi.dtos;

import jakarta.validation.Valid;
import lombok.Builder;

@Builder
public record Locations(
        @Valid
        LocationDTO current,
        @Valid
        LocationDTO previous) {

}

