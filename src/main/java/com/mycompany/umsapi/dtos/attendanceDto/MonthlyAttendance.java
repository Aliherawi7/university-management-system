package com.mycompany.umsapi.dtos.attendanceDto;

import com.mycompany.umsapi.constants.AttendanceStatusName;
import lombok.Builder;

@Builder
public record MonthlyAttendance(
        Integer year,
        Integer month,
        Integer day,
        AttendanceStatusName attendanceStatusName,
        boolean isHoliday
) {
}
