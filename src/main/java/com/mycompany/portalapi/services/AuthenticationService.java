package com.mycompany.portalapi.services;

import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.constants.RoleName;
import com.mycompany.portalapi.dtos.AuthenticationRequest;
import com.mycompany.portalapi.dtos.LoginResponse;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.Role;
import com.mycompany.portalapi.models.User;
import com.mycompany.portalapi.repositories.UserRepository;
import com.mycompany.portalapi.security.JwtUtils;
import com.mycompany.portalapi.utils.BaseURI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final HttpServletRequest httpServletRequest;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse authenticate(AuthenticationRequest authenticationRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.email(), authenticationRequest.password())
        );
        User user = userRepository.findByEmail(authenticationRequest.email()).orElseThrow(() -> new ResourceNotFoundException("user not found with provided email"));
        String token = jwtUtils.generateToken(user.getEmail(), user.getRoles(), 30);
        return LoginResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue()+user.getId())
                .roles(user.getRoles().stream().map(item -> item.getRoleName().getValue()).toList())
                .token(jwtUtils.generateToken(user.getEmail(),user.getRoles(), 30))
                .build();
    }

    public void registerUser(User user){
        System.err.println(user);
        validateUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void validateUser(User user){
        System.out.println(user);
        if(user.getId() == null ||
                user.getName() == null ||
                user.getEmail() == null ||
                user.getLastname() == null ||
                user.getRoles() == null ||
                user.getPassword() == null)
            throw new IllegalArgumentException("invalid user info");
    }
}
