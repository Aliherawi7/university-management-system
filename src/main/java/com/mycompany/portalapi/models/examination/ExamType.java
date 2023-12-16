package com.mycompany.portalapi.models.examination;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamType {
    @SequenceGenerator(name = "exam_type_sequence", sequenceName = "exam_type_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exam_type_sequence")
    @Id
    private Long id;
    private String name;
    private String descriptions;
    private Double highestScore;
    private Double lowestScore;
    private Double percent;

}
