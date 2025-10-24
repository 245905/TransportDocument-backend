package com.tui.transport.models;

import com.tui.transport.models.helpers.TimeLoggedClass;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "app-orders")
public class Order extends TimeLoggedClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String referenceNumber;
    private String goodsDescription;

    private String loadCity;
    private String loadAddress;

    private String unloadCity;
    private String unloadAddress;

    private Long distance;

    private OrderRequirements orderRequirements;
    private OrderStatus orderStatus;

    private LocalDateTime finishDate;

    @ManyToOne
    private Driver driver;
    @ManyToOne
    private Truck truck;
    @ManyToOne
    private Trailer trailer;

    public enum OrderStatus {
        Finished,
        InProgress,
        Cancelled,
        OnHold,
        Issue
    }
}
