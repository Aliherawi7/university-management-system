package com.mycompany.portalapi.models.hrms;


import jakarta.persistence.*;

@Entity
public class Job {

    @SequenceGenerator(name = "job_sequence", sequenceName = "job_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_sequence")
    @Id
    private Long id;
    private String jobTitle;
    private String descriptions;
    private Boolean isActive;


}
