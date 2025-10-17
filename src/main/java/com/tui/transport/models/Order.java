package com.tui.transport.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String orderNumber;
    private String orderType;
    private String orderStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;

    private String destinationAddress;
    private LocalDateTime orderDate;

    private String startingAddress;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Attachment> attachments;

    @Nullable
    @ManyToOne
    private User driver;

    @PrePersist
    void onCreate() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
    }
}
