package com.tui.transport.dto;


import jakarta.annotation.Nullable;
import lombok.Data;


@Data
public class UserResetPasswordDto {
    private String email;
    @Nullable
    private String token;
    @Nullable
    private String newPassword;
}
