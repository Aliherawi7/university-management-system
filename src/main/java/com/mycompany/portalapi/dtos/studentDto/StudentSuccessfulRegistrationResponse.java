package com.mycompany.portalapi.dtos.studentDto;

import lombok.Builder;

@Builder
public record StudentSuccessfulRegistrationResponse(
        Long studentId,
        String imageUrl,
        String message,
        Integer statusCode
) {
}
