package com.mycompany.umsapi.models.financial;

import com.mycompany.umsapi.models.hrms.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherContract {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    private Integer creditPerWeek;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime dailyStartTime;
    private LocalTime dailyEndTime;
    @ManyToOne
    @JoinColumn(name = "basic_salary_id")
    private BasicSalary basicSalary;



}
