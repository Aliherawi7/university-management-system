package com.mycompany.portalapi.controllers;


import com.mycompany.portalapi.dtos.APIResponse;
import com.mycompany.portalapi.dtos.StudentRegistrationDTO;
import com.mycompany.portalapi.services.FileStorageService;
import com.mycompany.portalapi.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/students")
public class StudentController {
    private final FileStorageService fileStorageService;
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody StudentRegistrationDTO studentRegistrationDTO) {
        return new ResponseEntity<>(studentService.addStudentForController(studentRegistrationDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable Long studentId, @RequestBody StudentRegistrationDTO studentRegistrationDTO) {
        return ResponseEntity.ok(studentService.updateStudent(studentId, studentRegistrationDTO));
    }

    /* ************************ search section *************************** */

    /* search by name */
    @GetMapping(value = "/")
    public ResponseEntity<?> getStudentsByName(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "fieldOfStudy", required = false) String fieldOfStudy,
            @RequestParam(name = "department", required = false) String department,
            @RequestParam(name = "semester", required = false) Integer semester,
            @RequestParam(name = "offset") Integer offset,
            @RequestParam(name = "pageSize") Integer pageSize
    ) {
        return ResponseEntity.ok().body(studentService.getAllPostsByRequestParams(keyword, fieldOfStudy, department, semester, offset, pageSize));
    }


}
