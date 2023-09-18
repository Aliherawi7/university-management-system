package com.mycompany.portalapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Attendance implements Comparable {
    @Id
    @SequenceGenerator(sequenceName = "attendance_sequence", name = "attendance_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendance_sequence")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "attendance_status")
    private AttendanceStatus attendanceStatus;
    private Integer yearNumber;
    private Integer monthNumber;
    private Integer dayNumber;
    private String fieldOfStudy;
    private String department;
    private String subject;
    private Integer semester;
    private Long studentId;


    @Override
    public int compareTo(Object o) {
        Attendance attendance = (Attendance) o;
        return this.dayNumber.compareTo(attendance.dayNumber);
    }
}
