package com.mycompany.portalapi.dtos;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record Locations(
        @Valid
        LocationDTO current,
        @Valid
        LocationDTO previous) {

}

