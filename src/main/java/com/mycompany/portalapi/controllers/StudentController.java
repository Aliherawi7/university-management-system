package com.mycompany.portalapi.controllers;


import com.mycompany.portalapi.dtos.APIResponse;
import com.mycompany.portalapi.dtos.studentDto.StudentRegistrationDTO;
import com.mycompany.portalapi.services.FileStorageService;
import com.mycompany.portalapi.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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
            @RequestParam(name = "faculty", required = false) Long facultyId,
            @RequestParam(name = "department", required = false) Long departmentId,
            @RequestParam(name = "semester", required = false) Long semester,
            @RequestParam(name = "offset") Integer offset,
            @RequestParam(name = "pageSize") Integer pageSize
    ) {
        return ResponseEntity.ok()
                .body(studentService.
                        getAllStudentsByRequestParams(keyword, facultyId, departmentId, semester, offset, pageSize));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long studentId){
        System.out.println("studentId: "+studentId);
        studentService.deleteStudentById(studentId);
        return ResponseEntity.ok(
                APIResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("محصل مورد نظر با موفقیت حذف شد!")
                        .httpStatus(HttpStatus.OK)
                        .zonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                        .build()
        );
    }
    
}
