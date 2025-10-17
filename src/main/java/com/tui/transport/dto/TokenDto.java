package com.tui.transport.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenDto {
    private String token;
    @Nullable
    private String resetKey;
    private LocalDateTime expiryDate;
}
