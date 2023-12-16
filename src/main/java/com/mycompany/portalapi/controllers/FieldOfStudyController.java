package com.mycompany.portalapi.controllers;


import com.mycompany.portalapi.dtos.DepartmentDTO;
import com.mycompany.portalapi.dtos.FieldOfStudyDTO;
import com.mycompany.portalapi.dtos.FacultyResponseDTO;
import com.mycompany.portalapi.services.DepartmentService;
import com.mycompany.portalapi.services.FacultyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/field-of-studies")
public class FieldOfStudyController {
    private final FacultyService facultyService;
    private final DepartmentService departmentService;

    @GetMapping("{id}")
    public ResponseEntity<?> getFieldOfStudy(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<FacultyResponseDTO>> getAllFieldOfStudies(){
        return ResponseEntity.ok(facultyService.getAllFieldOfStudies());
    }

    @PostMapping
    public void addFieldOfStudy(FieldOfStudyDTO fieldOfStudy){
        if(fieldOfStudy == null){
            throw new IllegalArgumentException("field of study should not be empty or null!");
        }
        facultyService.addFieldOfStudy(fieldOfStudy);
    }

    @PostMapping("{fieldStudyId}/departments")
    public ResponseEntity<?> addDepartment(@PathVariable Long fieldStudyId, @RequestBody DepartmentDTO departmentDTO){
        departmentService.addDepartment(departmentDTO);
        return ResponseEntity.ok("department added successfully");
    }

    @GetMapping("{fieldStudyId}/departments")
    public ResponseEntity<?> getAllDepartmentByFieldStudyId(@PathVariable Long fieldStudyId){
        return ResponseEntity.ok(departmentService.getAllDepartmentByFieldStudyId(fieldStudyId));
    }
    @GetMapping("{fieldOfStudyId}/departments/{departmentId}")
    public ResponseEntity<?> getFieldOfStudy(@PathVariable Long fieldOfStudyId, @PathVariable Long departmentId) {
        return ResponseEntity.ok(departmentService.getDepartmentByDepartmentIdAndFieldStudyId(departmentId, fieldOfStudyId));
    }





}
