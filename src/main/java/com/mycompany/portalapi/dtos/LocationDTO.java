package com.mycompany.portalapi.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
public record LocationDTO(
        @NonNull
        String villageOrQuarter,
        @NonNull
        String district,
        @NonNull
        String city,
        @NonNull
        Boolean current) {
}
