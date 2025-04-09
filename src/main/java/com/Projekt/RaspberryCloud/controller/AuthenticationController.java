package com.Projekt.RaspberryCloud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Projekt.RaspberryCloud.dto.request.LoginUserDto;
import com.Projekt.RaspberryCloud.model.User;
import com.Projekt.RaspberryCloud.service.AuthenticationService;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/api/login")
    public ResponseEntity<User> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authUser = authenticationService.authenticate(loginUserDto);
        // TODO: Add JWT
        return ResponseEntity.ok(authUser);
    }
}
