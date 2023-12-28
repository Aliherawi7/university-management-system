package com.mycompany.umsapi.dtos.studentDto;

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
        @NotNull(message = "حالت مدنی نباید خالی باشد")
        Integer maritalStatusId,
        @NotNull(message = "جنسیت نباید خالی باشد")
        Integer genderId,
        @NotBlank(message = "نام مکتب نباید خالی باشد")
        String highSchool,
        @NotBlank(message = "تاریخ فراغت  نباید خالی باشد")
        String schoolGraduationDate,
        @NotBlank(message = "تاریخ شمولیت  نباید خالی باشد")
        String joinedDate,
        @NotNull(message = "دیپارتمنت نباید خالی باشد")
        Long department,
        @NotBlank(message = "رمز حساب کاربری نباید خالی باشد")
        String password,
        @NotBlank(message = "ایمیل نباید خالی باشد")
        @Email(message = "ایمیل مورد نظر نامعتبر است")
        String email,
        @NotBlank(message = "شماره تماس نباید خالی باشد")
        String phoneNumber,
        @NotNull(message = "سمستر نباید خالی باشد")
        @Max(value = 14, message = "سمستر نباید بزرگتر از 10 باشد")
        @Min(value = 1, message = "سمستر نباید کوچکتر از 1 باشد")
        Integer semester
) {
}
