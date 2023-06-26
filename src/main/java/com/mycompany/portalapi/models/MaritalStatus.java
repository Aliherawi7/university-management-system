package com.mycompany.portalapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class MaritalStatus {
    @Id
    @SequenceGenerator(name = "maritalStatus_sequence", sequenceName = "maritalStatus_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "maritalStatus_sequence")
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "maritalStatus")
    private List<Student> studentList;
}
