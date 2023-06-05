package com.mycompany.portalapi.dtos;

import lombok.Builder;

@Builder
public record PostSuccessfulRegistrationDTO(Long postId,
                                            String filesUrl,
                                            String message,
                                            Integer statusCode) {
}
