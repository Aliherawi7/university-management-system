package com.mycompany.umsapi.dtos.attendanceDto;

import lombok.Builder;

@Builder
public record DayDetails(
        String dayOfWeek,
        Integer dayOfMonth,
        boolean isHoliday
) {
}
