package com.tui.transport.models;

import com.tui.transport.models.helpers.TimeLoggedClass;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Truck extends TimeLoggedClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String licensePlate;

    private String brand;
    private String model;
    private int year;

    @Column(unique = true)
    private String vin;

    private int weight;
    private int width;
    private int height;

    private FuelType fuelType;
    private String euroNorm;

    private long mileage;

    private float averageConsumption;

    @OneToOne
    private Driver currentDriver;

    public enum FuelType {
        Diesel,
        Gas,
        LNG,
        Electric,
        Hydrogen
    }
}
