package com.mycompany.umsapi.controllers;


import com.mycompany.umsapi.dtos.facultyDto.DepartmentDTO;
import com.mycompany.umsapi.dtos.facultyDto.FacultyDTO;
import com.mycompany.umsapi.dtos.facultyDto.FacultyResponseDTO;
import com.mycompany.umsapi.dtos.facultyDto.SemesterRegistrationDTO;
import com.mycompany.umsapi.models.faculty.Semester;
import com.mycompany.umsapi.services.DepartmentService;
import com.mycompany.umsapi.services.FacultyService;
import com.mycompany.umsapi.services.SemesterService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/faculties")
public class FacultyController {
    private final FacultyService facultyService;
    private final DepartmentService departmentService;
    private final SemesterService semesterService;

    @GetMapping("{id}")
    public ResponseEntity<?> getFieldOfStudy(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getFacultyResponseDTOById(id));
    }

    @GetMapping
    public ResponseEntity<Page<FacultyResponseDTO>> getAllFieldOfStudies() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @PostMapping
    public void addFaculty(FacultyDTO facultyDTO) {
        facultyService.addFaculty(facultyDTO);
    }


    @GetMapping("{facultyId}/departments")
    public ResponseEntity<?> getAllDepartmentByFacultyId(@PathVariable Long facultyId) {
        return ResponseEntity.ok(departmentService.getAllDepartmentByFacultyId(facultyId));
    }

    @GetMapping("{facultyId}/departments/{departmentId}")
    public ResponseEntity<?> getFaculty(@PathVariable Long facultyId, @PathVariable Long departmentId) {
        return ResponseEntity.ok(departmentService.getDepartmentByDepartmentIdAndFacultyId(departmentId, facultyId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFacultyById(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.deleteFacultyById(id));
    }


    /* department */

    @PostMapping("{facultyId}/departments")
    public ResponseEntity<?> addDepartment(@RequestBody  DepartmentDTO departmentDTO, @PathVariable Long facultyId) {
        return ResponseEntity.ok(departmentService.addDepartmentForController(departmentDTO, facultyId));
    }

    @DeleteMapping("{facultyId}/departments/{departmentId}")
    public ResponseEntity<?> deleteDepartmentByIdAndFacultyId(@PathVariable Long facultyId, @PathVariable Long departmentId) {
        return ResponseEntity.ok(departmentService.deleteFacultyIdAndDepartmentById(facultyId, departmentId));
    }


    /* semester */

    @PostMapping("{facultyId}/departments/{departmentId}/semesters")
    public ResponseEntity<?> addSemester(@RequestBody SemesterRegistrationDTO semesterRegistrationDTO, @PathVariable Long facultyId, @PathVariable Long departmentId){
        return ResponseEntity.ok(semesterService.addSemester(semesterRegistrationDTO, facultyId, departmentId));
    }

    @GetMapping("{facultyId}/departments/{departmentId}/semesters")
    public ResponseEntity<?> getSemesters(@PathVariable Long facultyId, @PathVariable Long departmentId){
        return ResponseEntity.ok(semesterService.getSemestersByFacultyIdAndDepartmentId(facultyId, departmentId));
    }



}
