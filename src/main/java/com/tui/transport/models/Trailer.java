package com.tui.transport.models;

import com.tui.transport.models.helpers.TimeLoggedClass;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Trailer extends TimeLoggedClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licensePlate;

    private TrailerType type;

    private long maxCapacity;
    private long ownWeight;
    private long volume;

    private int year;
    public enum TrailerType {
        Refrigerated,      // Chłodnia
        DryVan,           // Standardowa naczepa kontenerowa
        Flatbed,          // Platforma
        Curtainsider,     // Firanka
        Tanker,           // Cysterna
        Lowboy,           // Niskopodwoziowa
        Dump,             // Wywrotka
        StepDeck,         // Z obniżonym podłożem
        Box,              // Skrzyniowa
        Livestock,        // Do przewozu zwierząt
        CarCarrier,       // Do przewozu samochodów
        Hopper            // Zasypowa
    }
}
