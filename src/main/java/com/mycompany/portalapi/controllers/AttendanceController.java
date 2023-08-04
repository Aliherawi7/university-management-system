package com.mycompany.portalapi.controllers;

import com.mycompany.portalapi.dtos.AttendanceStudentListResponse;
import com.mycompany.portalapi.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/attendances")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @GetMapping(value = "/", params = {"semester", "fieldOfStudy", "department", "subject", "year", "month"})
    public ResponseEntity<AttendanceStudentListResponse> getStudentAttendanceListByParams(
            @RequestParam Integer semester, @RequestParam String fieldOfStudy, @RequestParam String department,
            @RequestParam String subject, @RequestParam Integer year, @RequestParam Integer month
    ){
        return ResponseEntity.ok(attendanceService.getTheAttendanceList(
                fieldOfStudy,department,semester,subject,year,month
        ));
    }
}
