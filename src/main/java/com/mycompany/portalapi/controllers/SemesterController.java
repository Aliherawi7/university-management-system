package com.mycompany.portalapi.controllers;

import com.mycompany.portalapi.models.Semester;
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

    @GetMapping(value = "/search", params = {"semester", "field", "department"})
    public ResponseEntity<?> getSemesterBySemesterAndFieldOfStudyAndDepartment(@RequestParam Map<String, String> params){
        int semester = Integer.parseInt(params.get("semester"));
        return ResponseEntity.ok(
                semesterService.getSemesterBySemesterAndFieldOfStudyAndDepartment((long) semester, params.get("field"), params.get("department"))
        );
    }

}
