package com.mycompany.portalapi.models.communication;


import com.mycompany.portalapi.models.hrms.UserApp;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    @Id
    @SequenceGenerator(name = "channel_sequence", sequenceName = "channel_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "channel_sequence")
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    @ManyToMany
    @JoinTable(
            name = "user_channel",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserApp> subscribers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "admin_channel",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserApp> admins = new HashSet<>();


}
