package com.tui.transport.models;

import com.tui.transport.models.helpers.Certification;
import com.tui.transport.models.helpers.Period;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class DriverQualifications {
    @Embedded
    private Licence licence;
    @Embedded
    private Adr adr;
    @Embedded
    private Period medicalCertification;
    @Embedded
    private Certification psychotestCertification;
    @Embedded
    private Certification chemicalCertification;
    @Embedded
    private Certification temperatureCertification;
    @Embedded
    private Certification sanepidCertification;
}
