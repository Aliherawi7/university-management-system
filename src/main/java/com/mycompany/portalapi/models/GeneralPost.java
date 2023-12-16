package com.mycompany.portalapi.models;

import com.mycompany.portalapi.models.hrms.UserApp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Length;

import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralPost {

    @Id
    @SequenceGenerator(name = "general_post_sequence", sequenceName = "general_post_sequence", initialValue = 1)
    @GeneratedValue(generator = "general_post_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(columnDefinition = "TEXT",  length = Length.LONG)
    private String message;
    private ZonedDateTime dateTime;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserApp author;
    private boolean isHidden;
    private boolean isUpdated;
}
