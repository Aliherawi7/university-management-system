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
                .orElseThrow(() -> new ResourceNotFoundException("حساب کاربری باایمیل ارائه شده یافت نشد!"));
        if (!userApp.isEnabled()) {
            throw new AccountLockException("حساب کاربری شما قفل است. لطفا تا باز شدن آن توسط مدیر صبورا باشید");
        }
        return LoginResponse.builder()
                .userId(userApp.getId())
                .name(userApp.getName())
                .lastname(userApp.getLastname())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + userApp.getId() + ".png")
                .email(userApp.getEmail())
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
            throw new IllegalArgumentException("معلومات نامعتبر کاربر");
    }

    public void lockUserById(Long userId) {
        UserApp userApp = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId + "حساب کاربری با آی دی مورد نظر یافت نشد: "));
        userApp.setIsEnabled(false);
        userRepository.save(userApp);
    }

    public void unLockUserById(Long userId) {
        UserApp userApp = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("کاربر با آی دی ارایه شده یافت نشد!: " + userId));
        userApp.setIsEnabled(true);
        userRepository.save(userApp);
    }

    public boolean isEnable(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("کاربر با ایمیل مورد نظر یافت نشد!"))
                .isEnabled();
    }

    public LoginResponse updateUser(UpdateUserDTO updateUserDTO, HttpServletRequest httpServletRequest) {

        UserApp userApp = userRepository.findById(updateUserDTO.userId()).orElseThrow(() -> new ResourceNotFoundException(updateUserDTO.userId() + "کاربری با آی دی نمبر ارائه شده یافت نشد: "));
        String previousEmail = userApp.getEmail();
        if (!userApp.isEnabled()) {
            throw new AccountLockException("حساب کاربری شما قفل است. لطفا تا باز شدن آن توسط مدیر صبورا باشید");
        }
        if (userRepository.existsByEmail(updateUserDTO.email()) && !previousEmail.equalsIgnoreCase(updateUserDTO.email())) {
            throw new IllegalArgumentException("ایمیل قبلا توسط کابری دیگری استفاده شده است");
        }
        if (!previousEmail.equalsIgnoreCase(updateUserDTO.email())) {
            userApp.setEmail(updateUserDTO.email());
        }

        userApp.setPassword(passwordEncoder.encode(updateUserDTO.newPassword()));
        userRepository.save(userApp);

        Student studentByEmail = studentRepository.findByEmail(previousEmail)
                .orElseThrow(() -> new ResourceNotFoundException("کاربر با ایمیل مورد نظر یافت نشد!"));
        studentByEmail.setEmail(updateUserDTO.email());
        studentRepository.save(studentByEmail);

        return LoginResponse.builder()
                .userId(userApp.getId())
                .name(userApp.getName())
                .lastname(userApp.getLastname())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + userApp.getId() + ".png")
                .roles(userApp.getRoles().stream().map(item -> item.getRoleName().getValue()).toList()).email(userApp.getEmail()).token(jwtUtils.generateToken(userApp.getEmail(), userApp.getRoles(), 30))
                .build();
    }

}
