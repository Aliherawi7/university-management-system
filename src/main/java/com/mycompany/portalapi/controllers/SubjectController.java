package com.mycompany.portalapi.controllers;

import com.mycompany.portalapi.dtos.CollectionResponse;
import com.mycompany.portalapi.models.Subject;
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
    public ResponseEntity<?> addSubject(@RequestBody Subject subject){
        subjectService.addSubject(subject);
        return ResponseEntity.ok("subject added successfully");
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getSubjectById(@PathVariable Long id){
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @GetMapping(value = "/search", params = {"field", "department"})
    public ResponseEntity<?> getAllSubjectByFieldAndDepartment(@RequestParam Map<String, String> params){
        Collection<Subject> subjects = subjectService.search(params.get("field"), params.get("department"));
        return ResponseEntity.ok(
                new CollectionResponse<Subject>(
                        subjects.size(),
                        subjects
                )
        );
    }
    @GetMapping(value = "/search", params = {"field"})
    public ResponseEntity<?> getAllSubjectByFieldAndDepartment(@RequestParam(value = "field") String field){
        Collection<Subject> subjects = subjectService.search(field);
        return ResponseEntity.ok(
                new CollectionResponse<Subject>(
                        subjects.size(),
                        subjects
                )
        );
    }
}
