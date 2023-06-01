package com.mycompany.portalapi.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Identification {

    @Id
    private Long nationalId;
    private Long studentId;
    private Long caseNumber;
    private Long pageNumber;
    private Long registrationNumber;

}
