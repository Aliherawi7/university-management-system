package com.mycompany.umsapi.models.hrms;


import com.mycompany.umsapi.models.faculty.Department;
import com.mycompany.umsapi.models.faculty.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String registrationId;
    private String name;
    private String lastName;
    private String fatherName;
    private String grandFatherName;
    private LocalDate dob;
    private String motherTongue;
    private String highSchool;
    private LocalDate schoolGraduationDate;
    private LocalDate joinedDate;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    private String email;
    private String password;
    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;
    private String phoneNumber;
    @OneToMany
    @JoinColumn(name = "student_id")
    private Set<Location> locations = new HashSet<>();
    @OneToMany(mappedBy = "student")
    private List<Relative> relatives;
    @OneToOne
    @JoinColumn(name = "national_id")
    private Identification identification;
    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;
    @ManyToOne
    @JoinColumn(name = "marital_status_id")
    private MaritalStatus maritalStatus;

}
