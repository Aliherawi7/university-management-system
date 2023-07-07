package com.mycompany.portalapi.controllers;

import com.mycompany.portalapi.dtos.AuthenticationRequest;
import com.mycompany.portalapi.dtos.LoginResponse;
import com.mycompany.portalapi.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("authenticate")

    public ResponseEntity<LoginResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @PutMapping("lock/{userId}")
    public ResponseEntity<?> lockUserById(@PathVariable Long userId) {
        authenticationService.lockUserById(userId);
        return ResponseEntity.ok("user account with id: {" + userId + "} has been locked successfully");
    }
    @PutMapping("unLock/{userId}")
    public ResponseEntity<?> unLockUserById(@PathVariable Long userId) {
        authenticationService.unLockUserById(userId);
        return ResponseEntity.ok("user account with id: {" + userId + "}has been unlocked successfully");
    }
}
