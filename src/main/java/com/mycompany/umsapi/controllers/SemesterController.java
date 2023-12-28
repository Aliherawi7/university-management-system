package com.mycompany.umsapi.controllers;

import com.mycompany.umsapi.dtos.facultyDto.SemesterDTO;
import com.mycompany.umsapi.services.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/semesters")
@RequiredArgsConstructor
public class SemesterController {
    private final SemesterService semesterService;



}
