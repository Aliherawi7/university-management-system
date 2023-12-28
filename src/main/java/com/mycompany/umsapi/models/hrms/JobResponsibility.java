package com.mycompany.umsapi.models.hrms;


import jakarta.persistence.*;

@Entity
public class JobResponsibility {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    private String descriptions;

}
