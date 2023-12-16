package com.mycompany.portalapi.models.hrms;


import com.mycompany.portalapi.models.financial.BasicSalary;
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
    @SequenceGenerator(name = "staff_sequence", sequenceName = "staff_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_sequence" )
    @Id
    private Long id;
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
