package com.mycompany.portalapi.dtos;

import lombok.Builder;

@Builder
public record AuthorDTO(
        String name,
        String lastname,
        String imageUrl

) {
}
