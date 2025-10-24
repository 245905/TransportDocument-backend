package com.tui.transport.models;

import com.tui.transport.models.helpers.Location;
import com.tui.transport.models.helpers.TimeLoggedClass;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Event extends TimeLoggedClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;
    private EventType eventType;
    private String description;

    private long odometer;
    private long fuelLiters;

    private Location location;

    private LocalDateTime eventTime;

    public enum EventType {
        LOADING,
        UNLOADING,
        FUEL,
        BREAKDOWN,
        BORDER,
        WAITING,
        OTHER

    }
}
