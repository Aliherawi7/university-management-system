package com.mycompany.umsapi.models.hrms;

import com.mycompany.umsapi.models.communication.Channel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserApp implements UserDetails {
    @Id
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    private Boolean isEnabled;
    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender genderName;

    @ManyToMany(mappedBy = "subscribers")
    private Set<Channel> subscribedChannels = new HashSet<>();

    @ManyToMany(mappedBy = "admins")
    private Set<Channel> asAdminChannel = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "user_type_id")
    private UserType userType;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(item -> new SimpleGrantedAuthority(item.getRoleName().getValue())).toList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
