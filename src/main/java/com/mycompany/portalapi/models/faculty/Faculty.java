package com.mycompany.portalapi.models.faculty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Faculty {
    @Id
    @SequenceGenerator(name = "faculty_sequence", initialValue = 1, sequenceName = "faculty_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faculty_sequence")
    private Long id;
    private String facultyName;
    private String description;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "faculty_id")
    private Set<Department> departments = new HashSet<>();

}