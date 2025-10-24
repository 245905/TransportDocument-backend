package com.tui.transport.models;

import com.tui.transport.models.helpers.Period;
import jakarta.persistence.*;

@Entity
public class DriverTruckLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Driver driver;

    @ManyToOne(fetch = FetchType.EAGER)
    private Truck truck;

    private Period period;
}
