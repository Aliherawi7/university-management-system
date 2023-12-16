package com.mycompany.portalapi.models.financial;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.util.Currency;
import java.util.Locale;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BasicSalary {

    @SequenceGenerator(name = "basic_salary_sequence", sequenceName = "basic_salary_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "basic_salary_sequence")
    @Id
    private Long id;
    private Double amount;
    private String currency;

}
