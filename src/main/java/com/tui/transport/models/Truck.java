package com.tui.transport.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String licensePlate;
    private String model;
    //Todo add more fields

    @OneToOne
    private User currentDriver;
}
