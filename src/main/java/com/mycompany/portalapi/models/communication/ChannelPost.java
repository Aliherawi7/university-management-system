package com.mycompany.portalapi.models.communication;


import com.mycompany.portalapi.models.hrms.UserApp;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "channel_post_sequence")
    @SequenceGenerator(name = "channel_post_sequence", sequenceName = "channel_post_sequence", initialValue = 1)
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
