package com.mycompany.portalapi.models.hrms;

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
public class Location {
    @Id
    @SequenceGenerator(name = "location_sequence", sequenceName = "location_sequence"  , initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_sequence")
    private Long id;
    private String villageOrQuarter;
    private String district;
    private String city;
    private boolean current;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

}
