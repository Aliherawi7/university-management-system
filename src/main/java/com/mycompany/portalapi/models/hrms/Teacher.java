package com.mycompany.portalapi.models.hrms;

import com.mycompany.portalapi.models.faculty.Department;
import com.mycompany.portalapi.models.faculty.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Teacher {

    @Id
    @SequenceGenerator(name = "teacher_sequence", sequenceName = "teacher_sequence", initialValue = 1)
    @GeneratedValue(generator = "teacher_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String lastname;
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;
    private int experience;
    private String email;
    private String password;
    @ManyToMany(mappedBy = "teachers")
    private Set<Department> departments = new HashSet<>();
    @ManyToMany(mappedBy = "teachers")
    private List<Subject> subjects = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Set<Location> locations = new HashSet<>();
    @OneToOne
    @JoinColumn(name = "national_id")
    private Identification identification;

}