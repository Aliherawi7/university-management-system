package com.mycompany.portalapi.controllers;


import com.mycompany.portalapi.dtos.StudentRegistrationDTO;
import com.mycompany.portalapi.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody StudentRegistrationDTO studentRegistrationDTO){
        studentService.addStudent(studentRegistrationDTO);
        return ResponseEntity.ok("student successfully save");
    }

    @GetMapping("{studentId}")
    public ResponseEntity<?> getStudent(@PathVariable Long studentId){
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

}
