package com.mycompany.portalapi.models.faculty;


import com.mycompany.portalapi.models.hrms.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Class {
    @SequenceGenerator(sequenceName = "class_sequence", name = "class_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "class_sequence")
    @Id
    private Long id;
    private String name;
    private String descriptions;
    @ManyToOne
    @JoinColumn(name = "current_semester_id")
    private Semester currentSemester;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @OneToMany
    @JoinColumn(name = "student_id")
    private Set<Student> students;
    private LocalDate startDate;
    private Boolean isActive;

}
