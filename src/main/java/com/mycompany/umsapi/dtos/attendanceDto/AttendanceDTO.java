package com.mycompany.umsapi.dtos.attendanceDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AttendanceDTO(
        @NotBlank(message = "وضعیت حاضری نباید خالی باشد!")
         Integer attendanceStatusId,
        @NotBlank(message = "سال نباید خالی باشد")
         Integer yearNumber,
        @NotBlank(message = "ماه نباید خالی باشد")
         Integer monthNumber,
        @NotBlank(message = "روز نباید خالی باشد")
         Integer dayNumber,
        @NotBlank(message = "رشته تحصیلی نباید خالی باشد")
         Long fieldOfStudy,
        @NotBlank(message = "دیپارتمنت نباید خالی باشد")
         Long department,
        @NotBlank(message = "مضمون نباید خالی باشد")
         Long subject,
        @NotBlank(message = "سمستر نباید خالی باشد")
         Long semesterId,
        @NotBlank(message = "آی دی محصل نباید خالی باشد")
         Long studentId
) {
}
