package com.mycompany.portalapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record StudentPersonalInfo(
        @NotNull(message = "name must not be null")
        @NotBlank(message = "name must not be empty")
        String name,
        @NotNull(message = "lastName must not be null")
        @NotBlank(message = "lastName must not be empty")
        String lastName,
        @NotNull(message = "fatherName must not be null")
        @NotBlank(message = "fatherName must not be empty")
        String fatherName,
        @NotNull(message = "grandFatherName must not be null")
        @NotBlank(message = "grandFatherName must not be empty")
        String grandFatherName,
        @NotNull(message = "dob must not be null")
        @NotBlank(message = "dob must not be empty")
        String dob,
        @NotNull(message = "motherTongue must not be null")
        @NotBlank(message = "motherTongue must not be empty")
        String motherTongue,
        @NotNull(message = "maritalStatus must not be null")
        @NotEmpty(message = "maritalStatus must not be empty")
        String maritalStatus,
        @NotNull(message = "gender must not be null")
        @NotEmpty(message = "gender must not be empty")
        String gender,
        @NotNull(message = "name must not be null")
        @NotBlank(message = "highSchool must not be empty")
        String highSchool,
        @NotNull(message = "schoolGraduationDate must not be null")
        @NotBlank(message = "schoolGraduationDate must not be empty")
        String schoolGraduationDate,
        @NotNull(message = "fieldOfStudy must not be null")
        @NotBlank(message = "fieldOfStudy must not be empty")
        String fieldOfStudy,
        @NotNull(message = "department must not be null")
        @NotBlank(message = "department must not be empty")
        String department,
        @NotNull(message = "password must not be null")
        @NotBlank(message = "password must not be empty")
        String password,
        @NotNull(message = "email must not be null")
        @NotBlank(message = "email must not be empty")
        @Email(message = "invalid email")
        String email,
        @NotNull(message = "phoneNumber must not be null")
        @NotEmpty(message = "phoneNumber must not be empty")
        String phoneNumber,
        @NotNull(message = "semester must not be null")
        @Max(value = 8, message = "semester value must not be greater than 8")
        @Min(value = 1, message = "semester value must not be lower than 1")
        Integer semester
) {
}
