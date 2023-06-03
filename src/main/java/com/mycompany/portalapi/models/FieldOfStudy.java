package com.mycompany.portalapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class FieldOfStudy {
    @Id
    @SequenceGenerator(name = "field_of_study_sequence", initialValue = 1, sequenceName = "field_of_study_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "field_of_study_sequence")
    private Long id;
    private String fieldName;
    private String description;

}