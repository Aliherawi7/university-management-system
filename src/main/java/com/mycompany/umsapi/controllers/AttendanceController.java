package com.mycompany.umsapi.controllers;

import com.mycompany.umsapi.dtos.attendanceDto.AttendanceDTO;
import com.mycompany.umsapi.dtos.attendanceDto.StudentAttendanceListResponse;
import com.mycompany.umsapi.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @GetMapping(value = "/", params = {"semester", "faculty", "department", "subject", "year", "month"})
    public ResponseEntity<StudentAttendanceListResponse> getStudentAttendanceListByParams(
            @RequestParam Long semester, @RequestParam Long faculty, @RequestParam Long department,
            @RequestParam Long subject, @RequestParam Integer year, @RequestParam Integer month
    ){
        return ResponseEntity.ok(attendanceService.getTheAttendanceList(
                faculty,department,semester,subject,year,month
        ));
    }
}
