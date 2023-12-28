package com.mycompany.umsapi.models.examination;


import com.mycompany.umsapi.models.hrms.Student;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
