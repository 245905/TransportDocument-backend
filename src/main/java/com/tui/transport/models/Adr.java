package com.tui.transport.models;

import com.tui.transport.models.helpers.Certification;
import com.tui.transport.models.helpers.Period;
import jakarta.persistence.Embeddable;

@Embeddable
public class Adr {
    private String licenceNumber;
    private String licenceAuthority;
    private Period validityPeriod;
    private Certification class1Certification;
    private Certification class2Certification;
    private Certification class3Certification;
    private Certification class4Certification;
    private Certification class5Certification;
    private Certification class6Certification;
    private Certification class7Certification;
    private Certification class8Certification;
    private Certification class9Certification;
}
