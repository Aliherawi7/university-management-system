package com.mycompany.umsapi.dtos;

import lombok.Builder;

import java.util.Collection;

@Builder
public record CollectionResponse<T>(
        Integer recordCount,
        Collection<T> records
) {
}
