package com.mycompany.portalapi.models.attendance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStatus {
    @Id
    @SequenceGenerator(name = "attendance_status_sequence", sequenceName = "attendance_status_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendance_status_sequence")
    private Integer id;
    private String name;
}
