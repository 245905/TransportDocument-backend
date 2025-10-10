package com.tui.transport.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class UserLoginDto {
    @Nullable
    private String username;
    @Nullable
    private String email;
    private String password;
}
