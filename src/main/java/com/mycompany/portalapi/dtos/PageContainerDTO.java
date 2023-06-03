package com.mycompany.portalapi.dtos;

import lombok.Builder;

import java.util.List;
@Builder
public record PageContainerDTO<T>(
        long recordCount,
        List<T> records) {
}
