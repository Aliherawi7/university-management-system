package com.mycompany.umsapi.dtos.postDto;

import lombok.Builder;

import java.time.ZonedDateTime;
import java.util.List;

@Builder
public record PostResponseDTO(
        Long id,
        String message,
        String department,
        String fieldOfStudy,
        ZonedDateTime dateTime,
        AuthorDTO author,
        List<String> images,
        List<String> docs,
        Boolean isPublic,
        Integer semester,
        boolean isHidden,
        boolean isUpdated


) {
}
