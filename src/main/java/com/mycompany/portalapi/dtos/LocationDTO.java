package com.mycompany.portalapi.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
public record LocationDTO(
        String villageOrQuarter,
        String district,
        String city,
        boolean current) {
}
