package com.mycompany.portalapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Length;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @SequenceGenerator(name = "post_sequence", sequenceName = "post_sequence", initialValue = 1)
    @GeneratedValue(generator = "post_sequence", strategy = GenerationType.SEQUENCE)
    private Long id;
    private String fieldOfStudy;
    private String department;
    @Column(columnDefinition = "TEXT",  length = Length.LONG)
    private String message;
    private ZonedDateTime dateTime;
    private Long authorId;
    private Integer semester;
    private boolean isPublic;
    private boolean isHidden;
    private boolean isUpdated;
}
