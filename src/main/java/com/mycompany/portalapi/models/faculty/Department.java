package com.mycompany.portalapi.models.faculty;

import com.mycompany.portalapi.models.hrms.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Department {
    @Id
    @SequenceGenerator(sequenceName = "department_sequence", name = "department_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_sequence")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;
    private String departmentName;
    private String description;
    @OneToMany
    @JoinColumn(name = "department_id")
    private Set<Semester> semesters = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "subject_department",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "teacher_department",
            joinColumns = @JoinColumn(name = "department_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    private Set<Teacher> teachers = new HashSet<>();


}
