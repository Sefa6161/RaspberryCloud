package com.Projekt.RaspberryCloud.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.Projekt.RaspberryCloud.dto.request.RegisterUserDto;
import com.Projekt.RaspberryCloud.service.UserService;

@Controller
public class WebAuthenticationController {
    private final UserService userService;

    public WebAuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String webLoginpage(Authentication authentication) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(Authentication authentication) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/dashboard";
        }
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute RegisterUserDto registerUserDto) {
        userService.signup(registerUserDto);
        return "redirect:/login";
    }

}
