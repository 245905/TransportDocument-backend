package com.tui.transport.models.helpers;

import jakarta.persistence.Embeddable;


@Embeddable
public class Certification {
    private boolean isAquired = false;
    private Period validityPeriod;
}
