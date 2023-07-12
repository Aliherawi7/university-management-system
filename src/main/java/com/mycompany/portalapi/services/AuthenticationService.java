package com.mycompany.portalapi.services;

import com.mycompany.portalapi.constants.APIEndpoints;
import com.mycompany.portalapi.constants.RoleName;
import com.mycompany.portalapi.dtos.AuthenticationRequest;
import com.mycompany.portalapi.dtos.LoginResponse;
import com.mycompany.portalapi.dtos.StudentResponseDTO;
import com.mycompany.portalapi.dtos.UpdateUserDTO;
import com.mycompany.portalapi.exceptions.AccountLockException;
import com.mycompany.portalapi.exceptions.IllegalArgumentException;
import com.mycompany.portalapi.exceptions.ResourceNotFoundException;
import com.mycompany.portalapi.models.Student;
import com.mycompany.portalapi.models.UserApp;
import com.mycompany.portalapi.repositories.StudentRepository;
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
    private final StudentRepository studentRepository;

    public LoginResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.email().toLowerCase(), authenticationRequest.password())
        );
        UserApp userApp = userRepository
                .findByEmail(authenticationRequest.email()
                        .toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("user not found with provided email"));
        if (!userApp.isEnabled()) {
            throw new AccountLockException("حساب کاربری شما قفل است. لطفا تا باز شدن آن توسط مدیر صبورا باشید");
        }
        return LoginResponse.builder()
                .userId(userApp.getId())
                .name(userApp.getName())
                .lastname(userApp.getLastname())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + userApp.getId())
                .roles(userApp.getRoles().stream().map(item -> item.getRoleName().getValue()).toList())
                .token(jwtUtils.generateToken(userApp.getEmail(), userApp.getRoles(), 30))
                .build();
    }

    public void registerUser(UserApp userApp) {
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
                .orElseThrow(() -> new ResourceNotFoundException(userId + "حساب کاربری با آی دی مورد نظر یافت نشد: "));
        userApp.setIsEnabled(false);
        userRepository.save(userApp);
    }

    public void unLockUserById(Long userId) {
        UserApp userApp = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with provided id: " + userId));
        userApp.setIsEnabled(true);
        userRepository.save(userApp);
    }

    public LoginResponse updateUser(UpdateUserDTO updateUserDTO, HttpServletRequest httpServletRequest) {
        String previousEmail = jwtUtils.getUserEmailByJWT(jwtUtils.getToken(httpServletRequest));
        UserApp userApp = userRepository.findByEmail(previousEmail).orElseThrow(() -> new ResourceNotFoundException(previousEmail + "کاربری با ایمیل ارائه شده یافت نشد: "));
        if (!userApp.isEnabled()) {
            throw new AccountLockException("حساب کاربری شما قفل است. لطفا تا باز شدن آن توسط مدیر صبورا باشید");
        }
        if (userRepository.existsByEmail(updateUserDTO.email())) {
            throw new IllegalArgumentException("ایمیل قبلا توسط کابری دیگری استفاده شده است");
        }
        userApp.setEmail(updateUserDTO.email());
        userApp.setPassword(passwordEncoder.encode(updateUserDTO.newPassword()));
        if (userApp.getRoles().stream().anyMatch(role -> role.getRoleName().equals(RoleName.STUDENT.getValue()))) {
            Student studentByEmail = studentRepository.findByEmail(previousEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("student not found with provided email:" + previousEmail));
            studentByEmail.setEmail(updateUserDTO.email());
            studentRepository.save(studentByEmail);
        }
        return LoginResponse.builder()
                .userId(userApp.getId())
                .name(userApp.getName())
                .lastname(userApp.getLastname())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + userApp.getId())
                .roles(userApp.getRoles().stream().map(item -> item.getRoleName().getValue()).toList())
                .token(jwtUtils.generateToken(userApp.getEmail(), userApp.getRoles(), 30))
                .build();

    }

}
