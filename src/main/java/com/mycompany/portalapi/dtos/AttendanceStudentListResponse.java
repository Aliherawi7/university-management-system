package com.mycompany.portalapi.dtos;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
@Builder
public record AttendanceStudentListResponse(
        String fieldOfStudy,
        String department,
        String subject,
        List<StudentAttendanceResponse> students,
        int daysInMonth,
        LocalDate localDate
) {
}
