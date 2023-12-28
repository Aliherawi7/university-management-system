package com.mycompany.umsapi.services;

import com.mycompany.umsapi.constants.APIEndpoints;
import com.mycompany.umsapi.dtos.AuthenticationRequest;
import com.mycompany.umsapi.dtos.LoginResponse;
import com.mycompany.umsapi.dtos.UpdateUserDTO;
import com.mycompany.umsapi.exceptions.AccountLockException;
import com.mycompany.umsapi.models.hrms.UserApp;
import com.mycompany.umsapi.security.JwtUtils;
import com.mycompany.umsapi.utils.BaseURI;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final HttpServletRequest httpServletRequest;

    public LoginResponse authenticate(AuthenticationRequest authenticationRequest) {
        System.out.println("==> req: "+authenticationRequest.email() + " ==>: "+ authenticationRequest.password());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.email().toLowerCase(), authenticationRequest.password())
        );
        UserApp userApp = userService
                .getUserAppByeEmail(authenticationRequest.email());
        if (!userApp.isEnabled()) {
            throw new AccountLockException("Your account is locked! please wait until admin unlock it");
        }
        return LoginResponse.builder()
                .userId(userApp.getId())
                .name(userApp.getName())
                .lastname(userApp.getLastname())
                .imageUrl(BaseURI.getBaseURI(httpServletRequest) + APIEndpoints.STUDENT_PROFILE_IMAGE.getValue() + userApp.getId() + ".png")
                .email(userApp.getEmail())
                .roles(userApp.getRoles().stream().map(item -> item.getRoleName().getValue()).toList())
                .token(jwtUtils.generateToken(userApp.getEmail(), userApp.getRoles(), 30))
                .userType(userApp.getUserType())
                .build();
    }

    public LoginResponse updateUser(UpdateUserDTO updateUserDTO, HttpServletRequest httpServletRequest) {
        String email = jwtUtils.getUserEmailByJWT(jwtUtils.getToken(httpServletRequest).substring(7));
        return userService.updateUser(updateUserDTO, email);
    }

}
