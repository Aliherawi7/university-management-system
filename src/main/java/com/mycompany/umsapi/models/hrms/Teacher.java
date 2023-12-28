package com.mycompany.umsapi.models.hrms;

import com.mycompany.umsapi.models.faculty.Department;
import com.mycompany.umsapi.models.faculty.Subject;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String registrationId;
    private String name;
    private String lastname;
    private String phoneNumber;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gender_id")
    private Gender gender;
    private int experience;
    private String email;
    private String password;
    @ManyToMany(mappedBy = "teachers", cascade = CascadeType.ALL)
    private Set<Department> departments = new HashSet<>();
    @ManyToMany(mappedBy = "teachers", cascade = CascadeType.ALL)
    private List<Subject> subjects = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Set<Location> locations = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "national_id")
    private Identification identification;

}