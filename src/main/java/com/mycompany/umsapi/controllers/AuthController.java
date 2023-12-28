package com.mycompany.umsapi.controllers;


import com.mycompany.umsapi.dtos.AuthenticationRequest;
import com.mycompany.umsapi.dtos.LoginResponse;
import com.mycompany.umsapi.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }



}
