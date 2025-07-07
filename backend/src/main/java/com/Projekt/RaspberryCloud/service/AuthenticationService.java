package com.Projekt.RaspberryCloud.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.Projekt.RaspberryCloud.dto.request.LoginUserDto;
import com.Projekt.RaspberryCloud.model.User;
import com.Projekt.RaspberryCloud.repository.UserRepository;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public User authenticate(LoginUserDto loginUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getUsername(),
                        loginUserDto.getPassword()));

        return userRepository.findByUsername(loginUserDto.getUsername()).orElseThrow();
    }
}
