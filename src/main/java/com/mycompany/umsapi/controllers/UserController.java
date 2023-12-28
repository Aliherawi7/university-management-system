package com.mycompany.umsapi.controllers;

import com.mycompany.umsapi.dtos.APIResponse;
import com.mycompany.umsapi.dtos.AuthenticationRequest;
import com.mycompany.umsapi.dtos.LoginResponse;
import com.mycompany.umsapi.dtos.UpdateUserDTO;
import com.mycompany.umsapi.services.AuthenticationService;
import com.mycompany.umsapi.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authenticationService;
    private final UserService userService;


    @PutMapping("lock/{userId}")
    public ResponseEntity<?> lockUserById(@PathVariable Long userId) {
        userService.lockUserById(userId);
        return ResponseEntity.ok(
                APIResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Account has been deactivated successfully")
                        .zonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @PutMapping("unLock/{userId}")
    public ResponseEntity<?> unLockUserById(@PathVariable Long userId) {
        userService.unLockUserById(userId);
        return ResponseEntity.ok(
                APIResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Account has been activated successfully")
                        .zonedDateTime(ZonedDateTime.now(ZoneId.of("UTC")))
                        .statusCode(HttpStatus.OK.value())
                        .build()
                );
    }

    @GetMapping("{userId}/roles")
    public ResponseEntity<?> getUserRole(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserRolesById(userId));
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO updateUserDTO, HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(authenticationService.updateUser(updateUserDTO, httpServletRequest));
    }
}
