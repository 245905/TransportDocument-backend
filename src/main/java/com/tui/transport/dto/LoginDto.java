package com.tui.transport.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String phoneNumber;
    private String email;
    private String password;
    private boolean rememberMe = false;
}
