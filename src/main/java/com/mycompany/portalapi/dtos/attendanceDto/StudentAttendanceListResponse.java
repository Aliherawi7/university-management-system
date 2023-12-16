package com.mycompany.portalapi.dtos.attendanceDto;

import com.mycompany.portalapi.dtos.studentDto.StudentAttendanceResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
@Builder
public record StudentAttendanceListResponse(
        Long faculty,
        Long department,
        Long subject,
        List<StudentAttendanceResponse> students,
        int daysInMonth,
        List<DayDetails> monthDetails,
        int daysWithoutHolidays,
        LocalDate localDate
) {
}
