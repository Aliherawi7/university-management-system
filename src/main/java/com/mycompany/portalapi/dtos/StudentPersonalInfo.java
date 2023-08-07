package com.mycompany.portalapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record StudentPersonalInfo(
        @NotBlank(message = "نام نباید خالی باشد")
        String name,
        @NotBlank(message = "تخلص نباید خالی باشد")
        String lastName,
        @NotBlank(message = "نام پدر نباید خالی باشد")
        String fatherName,
        @NotBlank(message = "نام پدرپزرک نباید خالی باشد")
        String grandFatherName,
        @NotBlank(message = "تاریخ تولد نباید خالی باشد")
        String dob,
        @NotBlank(message = "زبان مادری نباید خالی باشد")
        String motherTongue,
        @NotBlank(message = "حالت مدنی نباید خالی باشد")
        String maritalStatus,
        @NotBlank(message = "جنسیت نباید خالی باشد")
        String gender,
        @NotBlank(message = "نام مکتب نباید خالی باشد")
        String highSchool,
        @NotBlank(message = "تاریخ فراغت  نباید خالی باشد")
        String schoolGraduationDate,
        @NotBlank(message = "تاریخ شمولیت  نباید خالی باشد")
        String joinedDate,
        @NotBlank(message = "پوهنحی نباید خالی باشد")
        String fieldOfStudy,
        @NotBlank(message = "دیپارتمنت نباید خالی باشد")
        String department,
        @NotBlank(message = "رمز حساب کاربری نباید خالی باشد")
        String password,
        @NotBlank(message = "ایمیل نباید خالی باشد")
        @Email(message = "ایمیل نامعتبر")
        String email,
        @NotBlank(message = "شماره تماس نباید خالی باشد")
        String phoneNumber,
        @NotNull(message = "سمستر نباید خالی باشد")
        @Max(value = 14, message = "سمستر نباید بزرگتر از 10 باشد")
        @Min(value = 1, message = "سمستر نباید کوچکتر از 1 باشد")
        Integer semester
) {
}
