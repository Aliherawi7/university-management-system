package com.mycompany.portalapi.dtos;

import lombok.Builder;

import java.util.Collection;
import java.util.Objects;

@Builder
public record CollectionResponse<T>(
        Integer recordCount,
        Collection<T> records
) {
}
