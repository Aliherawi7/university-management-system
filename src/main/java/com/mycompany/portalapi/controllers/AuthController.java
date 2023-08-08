package com.mycompany.portalapi.controllers;

import com.mycompany.portalapi.dtos.APIResponse;
import com.mycompany.portalapi.dtos.AuthenticationRequest;
import com.mycompany.portalapi.dtos.LoginResponse;
import com.mycompany.portalapi.dtos.UpdateUserDTO;
import com.mycompany.portalapi.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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
        return ResponseEntity.ok(
                APIResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("حساب کاربری مورد نظر با موفقیت غیر فعال شد!")
                        .zonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @PutMapping("unLock/{userId}")
    public ResponseEntity<?> unLockUserById(@PathVariable Long userId) {
        authenticationService.unLockUserById(userId);
        return ResponseEntity.ok(
                APIResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("حساب کاربری مورد نظر با موفقیت فعال شد!")
                        .zonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                        .statusCode(HttpStatus.OK.value())
                        .build()
                );
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO updateUserDTO, HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(authenticationService.updateUser(updateUserDTO, httpServletRequest));
    }
}
