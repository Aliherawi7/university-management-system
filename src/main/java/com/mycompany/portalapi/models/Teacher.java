package com.mycompany.portalapi.models;

import com.mycompany.portalapi.constants.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Gender gender;
    private int experience;
    private String email;
    private String password;
}
