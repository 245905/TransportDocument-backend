package com.tui.transport.dto;


import lombok.Data;


@Data
public class DriverResetPasswordDto {
    private String email;
    private String token;
    private String newPassword;
}
