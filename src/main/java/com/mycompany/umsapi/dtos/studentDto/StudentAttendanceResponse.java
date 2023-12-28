package com.mycompany.umsapi.dtos.studentDto;

import com.mycompany.umsapi.dtos.attendanceDto.MonthlyAttendance;
import lombok.Builder;

import java.util.List;

@Builder
public record StudentAttendanceResponse(
        Long studentId,
        String name,
        String fatherName,
        List<MonthlyAttendance> monthlyAttendance,
        int totalPresent,
        int totalAbsent

) {
}
