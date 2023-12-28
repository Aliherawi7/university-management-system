package com.mycompany.umsapi.models;

import com.mycompany.umsapi.models.hrms.UserApp;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
