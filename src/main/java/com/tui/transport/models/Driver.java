package com.tui.transport.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;

    private String birthPlace;
    private LocalDate dateOfBirth;

    private String country;

    @Column(unique = true)
    private String phoneNumber;
    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String language = "english";

    private String passwordHash;

    @Embedded
    private DriverQualifications driverQualifications;

    @OneToOne
    private Truck currentTruck;

}
