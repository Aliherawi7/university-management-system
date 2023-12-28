package com.mycompany.umsapi.models.hrms;


import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Job {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String jobTitle;
    private String descriptions;
    private Boolean isActive;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<JobResponsibility> responsibilities;

}
