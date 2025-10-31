package com.tui.transport.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenDto {
    private String token;
    private String resetKey;
    private LocalDateTime expiryDate;
}
