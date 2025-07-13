package com.Projekt.RaspberryCloud.controller;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.Projekt.RaspberryCloud.service.DataService;
import com.Projekt.RaspberryCloud.service.UserService;
import com.Projekt.RaspberryCloud.util.AccessValidator;

@Controller
public class WebController {
    private final DataService dataService;
    private final UserService userService;

    public WebController(DataService dataService, UserService userService) {
        this.dataService = dataService;
        this.userService = userService;
    }

    @GetMapping("")
    public String redirectToDashboard() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model,
            Authentication authentication) throws AccessDeniedException {

        if (!AccessValidator.canAccess(authentication.getName(), authentication)) {
            throw new AccessDeniedException("Access denied");
        }

        long fileCount = dataService.getFileCountForUser(authentication.getName());
        long totalSpace = userService.getTotalSpace();
        long freeSpace = userService.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        int usagePercent = (int) ((double) usedSpace / totalSpace * 100);

        model.addAttribute("fileCount", fileCount);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("usedSpace", formatBytes(usedSpace));
        model.addAttribute("totalSpace", formatBytes(totalSpace));
        model.addAttribute("usagePercent", usagePercent);

        return "dashboard";
    }

    @GetMapping("/api/dashboard")
    public ResponseEntity<Map<String, Object>> checkAccess(Authentication authentication) throws AccessDeniedException {
        if (authentication == null && !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();

        if (!AccessValidator.canAccess(username, authentication)) {
            throw new AccessDeniedException("Access denied");
        }

        long fileCount = dataService.getFileCountForUser(authentication.getName());
        long totalSpace = userService.getTotalSpace();
        long freeSpace = userService.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        int usagePercent = (int) ((double) usedSpace / totalSpace * 100);

        System.out.println("fileCount: " + fileCount);

        Map<String, Object> response = new HashMap<>();
        response.put("fileCount", fileCount);
        response.put("username", username);
        response.put("usedSpace", formatBytes(usedSpace));
        response.put("totalSpace", formatBytes(totalSpace));
        response.put("usagePercent", usagePercent);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/change_password")
    public String changePassword() {
        return "change_password";
    }

    @GetMapping("/change_username")
    public String changeUsername() {
        return "change_username";
    }

    private String formatBytes(long bytes) {
        double gb = bytes / (1024.0 * 1024 * 1024);
        return String.format("%.2f GB", gb);
    }

}