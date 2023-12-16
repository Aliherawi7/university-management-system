package com.mycompany.portalapi.controllers;

import com.mycompany.portalapi.dtos.CollectionResponse;
import com.mycompany.portalapi.models.faculty.Subject;
import com.mycompany.portalapi.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("api/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<?> addSubject(@RequestBody Subject subject) {
        subjectService.addSubject(subject);
        return ResponseEntity.ok("subject added successfully");
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }
    /*
    * search section
    * */
    @GetMapping(value = "/search", params = {"name"})
    public ResponseEntity<?> getAllSubjectByName(@RequestParam String name) {
        Collection<Subject> subjects = subjectService.searchByName(name);
        return ResponseEntity.ok(new CollectionResponse<Subject>(subjects.size(), subjects));
    }


    @GetMapping(value = "/search", params = {"department", "semester"})
    public ResponseEntity<?> getAllSubjectByFieldAndDepartment(@RequestParam Map<String, String> params) {
        Collection<Subject> subjects = subjectService
                .search(params.get("department"), Integer.parseInt(params.get("semester")));
        return ResponseEntity.ok(new CollectionResponse<Subject>(subjects.size(), subjects));
    }

    @GetMapping(value = "/search", params = {"department",})
    public ResponseEntity<?> getAllSubjectByFieldAndDepartmentAndSemester(@RequestParam Map<String, String> params) {
        Collection<Subject> subjects = subjectService.search(params.get("department"));
        return ResponseEntity.ok(new CollectionResponse<Subject>(subjects.size(), subjects));
    }

}
