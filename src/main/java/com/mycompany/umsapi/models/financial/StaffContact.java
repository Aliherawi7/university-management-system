package com.mycompany.umsapi.models.financial;

import com.mycompany.umsapi.models.hrms.Job;
import com.mycompany.umsapi.models.hrms.Staff;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StaffContact {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @OneToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
    private LocalDate startDate;
    private LocalDate endTime;
    private LocalTime dailyStartTime;
    private LocalTime dailyEndTime;
    @ManyToOne
    @JoinColumn(name = "basic_salary_id")
    private BasicSalary basicSalary;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

}
