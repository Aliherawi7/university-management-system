package com.mycompany.portalapi.controllers;


import com.mycompany.portalapi.dtos.StudentRegistrationDTO;
import com.mycompany.portalapi.services.FileStorageService;
import com.mycompany.portalapi.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("pagination/{offset}/{pageSize}")
    public ResponseEntity<?> getAllProducts(@PathVariable int offset, @PathVariable int pageSize) {
        return ResponseEntity.ok().body(studentService.getAllStudents(offset, pageSize));
    }

    /* ************************ search section *************************** */

    /* search by name */
    @GetMapping(value = "/find", params = {"name", "offset", "pageSize"})
    public ResponseEntity<?> getStudentsByName(@RequestParam Map<String, String> params) {
        int offset = Integer.parseInt(params.get("offset"));
        int pageSize = Integer.parseInt(params.get("pageSize"));
        String name = params.get("name");
        System.out.println("find method mapper");
        return ResponseEntity.ok().body(studentService.getAllStudentsByName(name, offset, pageSize));
    }


}
