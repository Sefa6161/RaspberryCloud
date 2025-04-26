package com.Projekt.RaspberryCloud.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Projekt.RaspberryCloud.dto.ChangePasswordDto;
import com.Projekt.RaspberryCloud.dto.ChangeUsernameDto;
import com.Projekt.RaspberryCloud.dto.request.RegisterUserDto;
import com.Projekt.RaspberryCloud.model.Data;
import com.Projekt.RaspberryCloud.model.User;
import com.Projekt.RaspberryCloud.repository.DataRepository;
import com.Projekt.RaspberryCloud.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserDetailsService userDetailsService;
    private final DataRepository dataRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService,
            DataRepository dataRepository) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.dataRepository = dataRepository;
    }

    public User signup(RegisterUserDto registerUserDto) {
        if (!registerUserDto.getPassword().equals(registerUserDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwoerter stimmen nicht ueberein!");
        }

        User user = new User();
        user.setUsername(registerUserDto.getUsername());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        user.setAdminFlag(registerUserDto.isAdminFlag());

        // create new directory for User
        String absolutePath = System.getProperty("user.home") + "/RaspberryCloud/Users/" + user.getUsername();
        File dir = new File(absolutePath);
        dir.mkdirs();

        return userRepository.save(user);
    }

    public void changePassword(
            ChangePasswordDto changePasswordDto,
            String authUsername) {

        User user = userRepository.findByUsername(authUsername)
                .orElseThrow(() -> new RuntimeException("User nicht gefunden"));

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Passwoerter stimmen nicht ueber ein");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        user.setPasswordChangeRequired(false);
        userRepository.save(user);

        // Authentication update to the new password
        Authentication oldAuth = SecurityContextHolder.getContext().getAuthentication();

        UserDetails updated = userDetailsService.loadUserByUsername(user.getUsername());
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                updated,
                oldAuth.getCredentials(),
                updated.getAuthorities());
        newAuth.setDetails(oldAuth.getDetails());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public void changeUsername(
            ChangeUsernameDto changeUsernameDto,
            String authUsername) {
        if (userRepository.findByUsername(changeUsernameDto.getNewUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = userRepository.findByUsername(authUsername)
                .orElseThrow(() -> new RuntimeException("Username not found"));

        final Path baseUsersDir = Paths.get(System.getProperty("user.home"), "RaspberryCloud", "Users");
        Path oldDir = baseUsersDir.resolve(user.getUsername());
        Path newDir = baseUsersDir.resolve(changeUsernameDto.getNewUsername());

        try {
            Files.move(oldDir, newDir, StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Umbenennen des Benutzerordners", e);
        }

        // Override "uploadedBy" flag of Data to the new username
        List<Data> userFiles = dataRepository.findByUploadUser(user.getUsername());
        for (Data data : userFiles) {
            data.setUploadUser(changeUsernameDto.getNewUsername());
        }

        user.setUsername(changeUsernameDto.getNewUsername());
        user.setUsernameChangeRequired(false);
        userRepository.save(user);

        // Authentication update to the new username
        Authentication oldAuth = SecurityContextHolder.getContext().getAuthentication();

        UserDetails updated = userDetailsService.loadUserByUsername(user.getUsername());
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                updated,
                oldAuth.getCredentials(),
                updated.getAuthorities());
        newAuth.setDetails(oldAuth.getDetails());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

}
