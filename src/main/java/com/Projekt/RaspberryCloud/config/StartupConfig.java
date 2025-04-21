package com.Projekt.RaspberryCloud.config;

import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.Projekt.RaspberryCloud.model.User;
import com.Projekt.RaspberryCloud.repository.UserRepository;

@Component
public class StartupConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StartupConfig(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByAdminFlagTrue().isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setAdminFlag(true);
            admin.setPasswordChangeRequired(true);
            admin.setUsernameChangeRequired(true);
            String absolutePath = System.getProperty("user.home") + "/RaspberryCloud/Users/" + "admin";
            File dir = new File(absolutePath);
            dir.mkdirs();
            userRepository.save(admin);
            System.out.println("Admin-User erstellt.");
        } else {
            System.out.println("Admin-User existiert bereits.");
        }
    }
}
