package com.mycompany.portalapi.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @SequenceGenerator(name = "subject_sequence", sequenceName = "subject_sequence", initialValue = 1)
    @GeneratedValue(generator = "subject_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private Integer semester;
    private String department;
    private String fieldOfStudy;
    private Integer credit;
}
