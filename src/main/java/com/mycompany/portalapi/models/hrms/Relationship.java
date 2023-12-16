package com.mycompany.portalapi.models.hrms;

import com.mycompany.portalapi.constants.RelationName;
import com.mycompany.portalapi.constants.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Relationship {
    @Id
    @SequenceGenerator(name = "relationship_sequence", sequenceName = "relationship_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "relationship_sequence")
    private Integer id;
    @Enumerated(EnumType.STRING)
    private RelationName relationName;
}
