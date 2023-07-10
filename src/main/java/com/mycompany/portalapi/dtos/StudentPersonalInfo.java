package com.mycompany.portalapi.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record StudentPersonalInfo(
        @NotNull(message = "نام نباید خالی باشد")
        String name,
        @NotNull(message = "تخلص نباید خالی باشد")
        String lastName,
        @NotNull(message = "نام پدر نباید خالی باشد")
        String fatherName,
        @NotNull(message = "نام پدرپزرک نباید خالی باشد")
        String grandFatherName,
        @NotNull(message = "تاریخ تولد نباید خالی باشد")
        String dob,
        @NotNull(message = "زبان مادری نباید خالی باشد")
        String motherTongue,
        @NotNull(message = "حالت مدنی نباید خالی باشد")
        String maritalStatus,
        @NotNull(message = "جنسیت نباید خالی باشد")
        String gender,
        @NotNull(message = "نام مکتب نباید خالی باشد")
        String highSchool,
        @NotNull(message = "تاریخ فراغت  نباید خالی باشد")
        String schoolGraduationDate,
        @NotNull(message = "پوهنحی نباید خالی باشد")
        String fieldOfStudy,
        @NotNull(message = "دیپارتمنت نباید خالی باشد")
        String department,
        @NotNull(message = "رمز حساب کاربری نباید خالی باشد")
        String password,
        @NotNull(message = "ایمیل نباید خالی باشد")
        @Email(message = "ایمیل نامعنبر")
        String email,
        @NotNull(message = "شماره تماس نباید خالی باشد")
        String phoneNumber,
        @NotNull(message = "سمستر نباید خالی باشد")
        @Max(value = 8, message = "سمستر نباید بزرگتر از 8 باشد")
        @Min(value = 1, message = "سمستر نباید کوچکتر از 1 باشد")
        Integer semester
) {
}
