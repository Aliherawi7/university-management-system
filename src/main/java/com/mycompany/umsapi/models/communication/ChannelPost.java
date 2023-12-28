package com.mycompany.umsapi.models.communication;


import com.mycompany.umsapi.models.hrms.UserApp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
    private Long views;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserApp Author;

//    private ChannelPoll channelPoll;

}
