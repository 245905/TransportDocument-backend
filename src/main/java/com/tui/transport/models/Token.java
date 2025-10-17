package com.tui.transport.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String token;
    @ManyToOne
    private User user;
    @Nullable
    private String resetKey;
    private LocalDateTime expiryDate;
}
