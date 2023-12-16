package com.mycompany.portalapi.models.faculty;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Semester {
    @Id
    @SequenceGenerator(name = "semester_sequence", sequenceName = "semester_sequence", initialValue = 1)
    @GeneratedValue(generator = "semester_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;
    private Integer semester;
    private Integer credit;
    private Integer subjectTotal;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Subject> subjects = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
