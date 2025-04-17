package com.Projekt.RaspberryCloud.service;

import java.io.File;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Projekt.RaspberryCloud.dto.request.RegisterUserDto;
import com.Projekt.RaspberryCloud.model.User;
import com.Projekt.RaspberryCloud.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
    }

    public User signup(RegisterUserDto registerUserDto) {
        User user = new User();
        user.setUsername(registerUserDto.getUserName());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        user.setAdminFlag(registerUserDto.isAdminFlag());

        // create new directory for User
        String absolutePath = System.getProperty("user.home") + "/RaspberryCloud/Users/" + user.getUsername();
        File dir = new File(absolutePath);
        dir.mkdirs();

        return userRepository.save(user);
    }

}
