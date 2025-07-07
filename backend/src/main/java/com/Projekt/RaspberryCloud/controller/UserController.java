package com.Projekt.RaspberryCloud.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Projekt.RaspberryCloud.dto.ChangePasswordDto;
import com.Projekt.RaspberryCloud.dto.ChangeUsernameDto;
import com.Projekt.RaspberryCloud.service.UserService;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/change_password")
    @ResponseBody
    public String changePassword(
            @ModelAttribute ChangePasswordDto changePasswordDto,
            Authentication authentication) {

        String authUsername = authentication.getName();

        userService.changePassword(changePasswordDto, authUsername);
        return "Passwort geaendert";
    }

    @PutMapping("/change_username")
    @ResponseBody
    public String putMethodName(
            @ModelAttribute ChangeUsernameDto changeUsernameDto,
            Authentication authentication) {

        String authUsername = authentication.getName();
        userService.changeUsername(changeUsernameDto, authUsername);
        return "Username geaendert";
    }
}
