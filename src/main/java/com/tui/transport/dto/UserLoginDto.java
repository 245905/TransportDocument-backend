package com.tui.transport.dto;

import lombok.Data;

@Data
public class UserLoginDto {
    private String phoneNumber;
    private String email;
    private String password;
}
