package com.mycompany.portalapi.dtos;

import com.mycompany.portalapi.models.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AttendanceDTO(
        @NotBlank(message = "وضعیت حاضری نباید خالی باشد!")
         String attendanceStatus,
        @NotBlank(message = "سال نباید خالی باشد")
         Integer yearNumber,
        @NotBlank(message = "ماه نباید خالی باشد")
         Integer monthNumber,
        @NotBlank(message = "روز نباید خالی باشد")
         Integer dayNumber,
        @NotBlank(message = "رشته تحصیلی نباید خالی باشد")
         String fieldOfStudy,
        @NotBlank(message = "دیپارتمنت نباید خالی باشد")
         String department,
        @NotBlank(message = "مضمون نباید خالی باشد")
         String subject,
        @NotBlank(message = "سمستر نباید خالی باشد")
         Integer semester,
        @NotBlank(message = "آی دی محصل نباید خالی باشد")
         Long studentId
) {
}
