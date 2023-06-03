package com.mycompany.portalapi.models;


import com.mycompany.portalapi.constants.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private Long nationalId;
    private MaritalStatus maritalStatus;
    private String highSchool;
    private LocalDate schoolGraduationDate;
    private LocalDate joinedDate;
    private String fieldOfStudy;
    private String department;
    private Long currentLocation;
    private Long previousLocation;
    private String email;
    private String password;

}
