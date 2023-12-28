package com.mycompany.umsapi.models.hrms;


import com.mycompany.umsapi.models.financial.BasicSalary;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Staff {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String registrationId;
    private String name;
    private String lastName;
    private String fatherName;
    @OneToOne
    @JoinColumn(name = "national_id")
    private Identification identification;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;
    @ManyToOne
    @JoinColumn(name = "salary_id")
    private BasicSalary salary;


}
