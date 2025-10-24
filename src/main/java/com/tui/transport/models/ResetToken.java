package com.tui.transport.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class ResetToken {
    @Id
    private String token;
    @ManyToOne
    private Driver driver;
    private LocalDateTime expiryDate;
}
