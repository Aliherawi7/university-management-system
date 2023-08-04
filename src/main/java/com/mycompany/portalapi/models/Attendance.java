package com.mycompany.portalapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Attendance implements Comparable {
    @Id
    @SequenceGenerator(sequenceName = "attendance_sequence", name = "attendance_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendance_sequence")
    private Long id;
    private boolean isPresent;
    private LocalDate localDate;
    private String fieldOfStudy;
    private String department;
    private String subject;
    private Integer semester;
    private Long studentId;


    @Override
    public int compareTo(Object o) {
        Attendance attendance = (Attendance) o;
        return this.getLocalDate().compareTo(attendance.getLocalDate());
    }
}
