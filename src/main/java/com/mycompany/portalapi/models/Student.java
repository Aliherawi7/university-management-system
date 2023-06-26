package com.mycompany.portalapi.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

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
    private String fieldOfStudy;
    private String department;
    private String email;
    private String password;
    private Integer semester;
    private String phoneNumber;
    @OneToMany(mappedBy = "student")
    private List<Location> locations;
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
