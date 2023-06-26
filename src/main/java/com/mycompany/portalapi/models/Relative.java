package com.mycompany.portalapi.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mycompany.portalapi.config.RelationDeserializer;
import com.mycompany.portalapi.constants.RelationName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Relative {

    @Id
    @SequenceGenerator(name = "relation_sequence", sequenceName = "relation_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "relation_sequence")
    private Long id;
    private String name;
    private String job;
    private String phoneNumber;
    private String jobLocation;
    @ManyToOne
    @JoinColumn(name = "relationship")
    private Relationship relationship;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}
