package com.mycompany.umsapi.dtos.postDto;

import lombok.Builder;

@Builder
public record AuthorDTO(
        String name,
        String lastname,
        String imageUrl

) {
}
