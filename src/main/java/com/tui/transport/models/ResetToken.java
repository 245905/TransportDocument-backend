package com.tui.transport.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
public class ResetToken {
    @Id
    @Column(length = 1024)
    private String token;
    @ManyToOne
    private Driver driver;
    private Instant expiryDate;
}
