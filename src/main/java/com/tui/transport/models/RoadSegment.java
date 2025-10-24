package com.tui.transport.models;

import com.tui.transport.models.helpers.Location;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RoadSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Report report;

    private int sequence; //Numer odcinka na trasie

    private Location startLocation;
    private Location endLocation;

    private int distance;
    private int fuelUsed;
    private int emission;
}
