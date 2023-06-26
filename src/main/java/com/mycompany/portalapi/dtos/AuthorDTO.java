package com.mycompany.portalapi.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AuthorDTO(
        String name,
        String lastname,
        String imageUrl

) {
}
