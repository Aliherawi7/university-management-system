package com.mycompany.portalapi.dtos.attendanceDto;

import lombok.Builder;

@Builder
public record DayDetails(
        String dayOfWeek,
        Integer dayOfMonth,
        boolean isHoliday
) {
}
