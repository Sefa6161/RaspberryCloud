package com.Projekt.RaspberryCloud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/dashboard")
    public String showDashboard() {
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