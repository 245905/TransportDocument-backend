package com.tui.transport.models;

import com.tui.transport.models.helpers.Certification;
import com.tui.transport.models.helpers.Period;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DriverQualifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
