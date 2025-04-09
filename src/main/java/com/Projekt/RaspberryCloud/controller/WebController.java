package com.Projekt.RaspberryCloud.controller;

import org.springframework.web.bind.annotation.RestController;

import com.Projekt.RaspberryCloud.dto.request.RegisterUserDto;
import com.Projekt.RaspberryCloud.model.User;
import com.Projekt.RaspberryCloud.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class WebController {

    private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDto registerUserDto) {
        User registerdUser = userService.signup(registerUserDto);
        return ResponseEntity.ok(registerdUser);
    }

}