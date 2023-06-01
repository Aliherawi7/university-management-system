package com.mycompany.portalapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Long fieldOfStudyId;
    private String departmentName;
    private String description;

}
