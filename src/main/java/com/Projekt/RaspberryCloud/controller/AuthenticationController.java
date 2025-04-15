package com.Projekt.RaspberryCloud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.Projekt.RaspberryCloud.dto.request.LoginUserDto;
import com.Projekt.RaspberryCloud.model.LoginResponseDto;
import com.Projekt.RaspberryCloud.model.User;
import com.Projekt.RaspberryCloud.service.AuthenticationService;
import com.Projekt.RaspberryCloud.service.JwtService;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/api/mobile/login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(jwtToken);
        loginResponseDto.setUsername(authenticatedUser.getUsername());

        return ResponseEntity.ok(loginResponseDto);
    }
}
