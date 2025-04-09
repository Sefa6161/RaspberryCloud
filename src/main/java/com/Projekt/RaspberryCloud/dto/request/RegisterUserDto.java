package com.Projekt.RaspberryCloud.dto.request;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String userName;
    private String email;
    private String password;
    private boolean adminFlag;
}
