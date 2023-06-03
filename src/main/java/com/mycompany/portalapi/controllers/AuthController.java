package com.mycompany.portalapi.controllers;

import com.mycompany.portalapi.dtos.AuthenticationRequest;
import com.mycompany.portalapi.dtos.LoginResponse;
import com.mycompany.portalapi.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticate;

    @PostMapping("authenticate")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(authenticate.authenticate(authenticationRequest));
    }
    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(AuthenticationRequest authenticationRequest){
        System.err.println("in login path");
        return ResponseEntity.ok(authenticate.authenticate(authenticationRequest));
    }
}
