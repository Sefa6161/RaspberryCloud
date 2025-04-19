package com.Projekt.RaspberryCloud.dto.request;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private boolean adminFlag;
}
