package com.mycompany.portalapi.dtos;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record Locations(@NonNull LocationDTO current, @NonNull LocationDTO previous) {

}

