package com.tui.transport.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Trailer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Truck truck;

    private long maxCapacity;
    private long weight;

    private String currentProducts;
}
