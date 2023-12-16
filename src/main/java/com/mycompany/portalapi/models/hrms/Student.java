package com.mycompany.portalapi.models.hrms;


import com.mycompany.portalapi.models.faculty.Department;
import com.mycompany.portalapi.models.faculty.Faculty;
import com.mycompany.portalapi.models.faculty.Semester;
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

    @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
    @Id
    private Long id;
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
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;
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
    @JoinColumn(name = "maritalStatus")
    private MaritalStatus maritalStatus;

}
