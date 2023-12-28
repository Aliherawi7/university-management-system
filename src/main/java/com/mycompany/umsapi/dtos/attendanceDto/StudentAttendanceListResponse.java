package com.mycompany.umsapi.dtos.attendanceDto;

import com.mycompany.umsapi.dtos.studentDto.StudentAttendanceResponse;
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
