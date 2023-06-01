package com.mycompany.portalapi.dtos;

import lombok.Builder;

@Builder
public record Locations(LocationDTO current, LocationDTO previous) {

}

