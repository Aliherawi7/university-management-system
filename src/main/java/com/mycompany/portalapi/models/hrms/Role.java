package com.mycompany.portalapi.models.hrms;

import com.mycompany.portalapi.constants.RoleName;
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
public class Role {
    @Id
    @SequenceGenerator(name = "role_sequence", sequenceName = "role_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_sequence")
    private Integer id;
    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
