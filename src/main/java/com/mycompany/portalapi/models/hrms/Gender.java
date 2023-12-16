package com.mycompany.portalapi.models.hrms;

import com.mycompany.portalapi.constants.GenderName;
import com.mycompany.portalapi.constants.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gender {
    @Id
    @SequenceGenerator(name = "gender_sequence", sequenceName = "gender_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gender_sequence")
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "gender")
    private List<Student> studentList;
}
