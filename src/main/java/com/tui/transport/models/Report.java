package com.tui.transport.models;

import com.tui.transport.models.helpers.TimeLoggedClass;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Report extends TimeLoggedClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Order order;

    public Long totalDistance;
    public Long totalFuel;
    public Long totalEmission;
}
