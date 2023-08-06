package com.mycompany.portalapi.dtos;

import lombok.Builder;

@Builder
public record MonthlyAttendance(
        Integer year,
        Integer month,
        Integer day,
        boolean isPresent,
        boolean isHoliday
) {
}
