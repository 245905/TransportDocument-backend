package com.tui.transport.models.helpers;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Location {
    private float x;
    private float y;
}
