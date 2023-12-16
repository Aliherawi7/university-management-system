package com.mycompany.portalapi.models.hrms;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Identification {

    @Id
    @SequenceGenerator(name = "identification_sequence", sequenceName = "identification_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "identification_sequence")
    private Long id;
    private Long nationalId;
    private Long caseNumber;
    private Long pageNumber;
    private Long registrationNumber;

}
