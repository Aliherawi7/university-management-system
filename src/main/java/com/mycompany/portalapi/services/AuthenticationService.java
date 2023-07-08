package com.mycompany.portalapi.services;

import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.dtos.AuthenticationRequest;
import com.mycompany.portalapi.dtos.LoginResponse;
import com.mycompany.portalapi.exceptions.AccountLockException;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.UserApp;
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

    public LoginResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.email(), authenticationRequest.password())
        );
        UserApp userApp = userRepository.findByEmail(authenticationRequest.email()).orElseThrow(() -> new ResourceNotFoundException("user not found with provided email"));
        if (!userApp.isEnabled()) {
            throw new AccountLockException("Your account is locked! wait until it is unlocked back by admin");
        }
        String token = jwtUtils.generateToken(userApp.getEmail(), userApp.getRoles(), 30);
        return LoginResponse.builder()
                .userId(userApp.getId())
                .name(userApp.getName())
                .lastname(userApp.getLastname())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + userApp.getId())
                .roles(userApp.getRoles().stream().map(item -> item.getRoleName().getValue()).toList())
                .token(token)
                .build();
    }

    public void registerUser(UserApp userApp) {
        System.err.println(userApp);
        validateUser(userApp);
        userApp.setPassword(passwordEncoder.encode(userApp.getPassword()));
        userRepository.save(userApp);
    }

    public void validateUser(UserApp userApp) {
        if (userApp.getId() == null ||
                userApp.getName() == null ||
                userApp.getEmail() == null ||
                userApp.getLastname() == null ||
                userApp.getRoles() == null ||
                userApp.getPassword() == null)
            throw new IllegalArgumentException("invalid user info");
    }

    public void lockUserById(Long userId) {
        UserApp userApp = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with provided id: " + userId));
        userApp.setIsEnabled(false);
        userRepository.save(userApp);
    }

    public void unLockUserById(Long userId) {
        UserApp userApp = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with provided id: " + userId));
        userApp.setIsEnabled(true);
        userRepository.save(userApp);
    }
}
