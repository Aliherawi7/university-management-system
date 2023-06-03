package com.mycompany.portalapi.dtos;

import lombok.Builder;

@Builder
public record StudentSuccessfulRegistrationResponse(
        Long studentId,
        String imageUrl,
        String message,
        Integer statusCode
) {
}
