package com.mycompany.portalapi.controllers;

import com.mycompany.portalapi.models.faculty.Semester;
import com.mycompany.portalapi.services.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/semesters")
@RequiredArgsConstructor
public class SemesterController {
    private final SemesterService semesterService;


    @PostMapping
    public ResponseEntity<?> addSemester(@RequestBody Semester semester){
        // to do
        semesterService.addSemester(semester);
        return ResponseEntity.ok("added ...");
    }

    @GetMapping(value = "/search", params = {"semester", "department"})
    public ResponseEntity<?> getSemesterBySemesterAndFieldOfStudyAndDepartment(@RequestParam Map<String, Long> params){
        return ResponseEntity.ok(
                semesterService.getSemesterBySemesterAndFieldOfStudyAndDepartment(params.get("semester"), params.get("department"))
        );
    }

}
