package com.mycompany.portalapi.models.examination;


import com.mycompany.portalapi.models.faculty.Class;
import com.mycompany.portalapi.models.faculty.Department;
import com.mycompany.portalapi.models.faculty.Semester;
import com.mycompany.portalapi.models.faculty.Subject;
import com.mycompany.portalapi.models.hrms.Teacher;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
public class ScoreSheet {
    @SequenceGenerator(name = "score_sheet_sequence", sequenceName = "score_sheet_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "score_sheet_sequence")
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
