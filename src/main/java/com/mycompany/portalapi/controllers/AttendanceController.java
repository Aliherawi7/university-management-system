package com.mycompany.portalapi.controllers;

import com.mycompany.portalapi.dtos.AttendanceDTO;
import com.mycompany.portalapi.dtos.AttendanceStudentListResponse;
import com.mycompany.portalapi.models.Attendance;
import com.mycompany.portalapi.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/attendances")
@RequiredArgsConstructor
@Slf4j
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<?> addAttendance(@RequestBody AttendanceDTO attendanceDTO){
        //log.info("ispr:{}, id:{}, studentId:{}",attendance.getIsPresent(), attendance.getId(), attendance.getStudentId());
        return ResponseEntity.ok(attendanceService.addAttendance(attendanceDTO));
    }
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
