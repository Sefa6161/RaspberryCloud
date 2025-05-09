package com.Projekt.RaspberryCloud.controller;

import java.nio.file.AccessDeniedException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.Projekt.RaspberryCloud.service.DataService;
import com.Projekt.RaspberryCloud.util.AccessValidator;

@Controller
public class WebController {
    private final DataService dataService;

    public WebController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model,
            Authentication authentication) throws AccessDeniedException {

        if (!AccessValidator.canAccess(authentication.getName(), authentication)) {
            throw new AccessDeniedException("Access denied");
        }

        long fileCount = dataService.getFileCountForUser(authentication.getName());

        model.addAttribute("fileCount", fileCount);
        model.addAttribute("username", authentication.getName());

        return "dashboard";
    }

    @GetMapping("/change_password")
    public String changePassword() {
        return "change_password";
    }

    @GetMapping("/change_username")
    public String changeUsername() {
        return "change_username";
    }

}