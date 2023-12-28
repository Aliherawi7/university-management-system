package com.mycompany.umsapi.models.examination;


import com.mycompany.umsapi.models.faculty.Class;
import com.mycompany.umsapi.models.faculty.Department;
import com.mycompany.umsapi.models.faculty.Semester;
import com.mycompany.umsapi.models.faculty.Subject;
import com.mycompany.umsapi.models.hrms.Teacher;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
public class ScoreSheet {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    @OneToMany(mappedBy = "scoreSheet")
    private Set<StudentScore> studentScores;
    @ManyToOne
    @JoinColumn(name = "exam_type_id")
    private ExamType examType;
    private LocalDate createdAt;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class studentClass;

}
