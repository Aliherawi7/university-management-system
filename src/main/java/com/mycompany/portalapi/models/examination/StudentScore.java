package com.mycompany.portalapi.models.examination;


import com.mycompany.portalapi.models.hrms.Student;
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
public class StudentScore {
    @SequenceGenerator(name = "student_score_sequence", sequenceName = "student_score_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_score_sequence")
    @Id
    private Long id;
    private Double score;
    @ManyToOne
    @JoinColumn(name = "score_sheet_id")
    private ScoreSheet scoreSheet;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

}
