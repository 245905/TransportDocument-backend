package com.tui.transport.models.helpers;

import jakarta.persistence.Embeddable;


@Embeddable
public class Certification {
    private boolean isAcquired = false;
    private Period validityPeriod;
}
