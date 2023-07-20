package com.mycompany.portalapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Data
@Builder
public class APIResponse {
    private ZonedDateTime zonedDateTime;
    private HttpStatus httpStatus;
    private int statusCode;
    private String message;
}


