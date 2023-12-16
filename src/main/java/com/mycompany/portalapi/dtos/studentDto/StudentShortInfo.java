package com.mycompany.portalapi.dtos.studentDto;

import lombok.Builder;

@Builder
public record StudentShortInfo(
        Long id,
        String name,
        String lastname,
        String fatherName,
        String faculty,
        String department,
        String imageUrl
) {
}
