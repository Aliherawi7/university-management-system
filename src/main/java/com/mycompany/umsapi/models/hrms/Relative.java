package com.mycompany.umsapi.models.hrms;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Relative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String job;
    private String phoneNumber;
    private String jobLocation;
    @ManyToOne
    @JoinColumn(name = "relationship_id")
    private Relationship relationship;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
