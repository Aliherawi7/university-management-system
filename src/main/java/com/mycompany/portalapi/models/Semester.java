package com.mycompany.portalapi.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.print.attribute.IntegerSyntax;
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
    private String department;
    private String fieldOfStudy;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Subject> subjects = new ArrayList<>();

}
