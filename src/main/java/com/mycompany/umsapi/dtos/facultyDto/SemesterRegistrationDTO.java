package com.mycompany.umsapi.dtos.facultyDto;

import com.mycompany.umsapi.models.faculty.Department;
import com.mycompany.umsapi.models.faculty.Subject;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public record SemesterRegistrationDTO(
         Integer semester,
         Integer credit,
         Integer subjectTotal,
         List<Subject> subjects,
         Department department,
         Double paymentAmount
) {
}