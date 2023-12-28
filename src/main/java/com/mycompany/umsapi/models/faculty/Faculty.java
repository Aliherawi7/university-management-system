package com.mycompany.umsapi.models.faculty;

import lombok.*;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String facultyName;
    private String description;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "faculty_id")
    private Set<Department> departments = new HashSet<>();

}