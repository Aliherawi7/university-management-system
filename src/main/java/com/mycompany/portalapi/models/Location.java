package com.mycompany.portalapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Location {
    @Id
    @SequenceGenerator(name = "location_sequence", sequenceName = "location_sequence"  , initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_sequence")
    private Long id;
    private Long studentId;
    private String villageOrQuarter;
    private String district;
    private String city;
    private boolean current;

}
