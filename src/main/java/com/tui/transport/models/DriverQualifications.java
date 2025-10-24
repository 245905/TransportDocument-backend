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
    @AttributeOverrides({
        @AttributeOverride(name = "licenceNumber", column = @Column(name = "lic_num")),
        @AttributeOverride(name = "licenceAuthority", column = @Column(name = "lic_auth")),
        @AttributeOverride(name = "validityPeriod.startDate", column = @Column(name = "lic_val_start")),
        @AttributeOverride(name = "validityPeriod.endDate", column = @Column(name = "lic_val_end")),
        @AttributeOverride(name = "B_Certification.isAcquired", column = @Column(name = "lic_b_acq")),
        @AttributeOverride(name = "B_Certification.validityPeriod.startDate", column = @Column(name = "lic_b_val_start")),
        @AttributeOverride(name = "B_Certification.validityPeriod.endDate", column = @Column(name = "lic_b_val_end")),
        @AttributeOverride(name = "C_Certification.isAcquired", column = @Column(name = "lic_c_acq")),
        @AttributeOverride(name = "C_Certification.validityPeriod.startDate", column = @Column(name = "lic_c_val_start")),
        @AttributeOverride(name = "C_Certification.validityPeriod.endDate", column = @Column(name = "lic_c_val_end")),
        @AttributeOverride(name = "CE_Certification.isAcquired", column = @Column(name = "lic_ce_acq")),
        @AttributeOverride(name = "CE_Certification.validityPeriod.startDate", column = @Column(name = "lic_ce_val_start")),
        @AttributeOverride(name = "CE_Certification.validityPeriod.endDate", column = @Column(name = "lic_ce_val_end")),
        @AttributeOverride(name = "D_Certification.isAcquired", column = @Column(name = "lic_d_acq")),
        @AttributeOverride(name = "D_Certification.validityPeriod.startDate", column = @Column(name = "lic_d_val_start")),
        @AttributeOverride(name = "D_Certification.validityPeriod.endDate", column = @Column(name = "lic_d_val_end"))
    })
    private Licence licence;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "licenceNumber", column = @Column(name = "adr_num")),
        @AttributeOverride(name = "licenceAuthority", column = @Column(name = "adr_auth")),
        @AttributeOverride(name = "validityPeriod.startDate", column = @Column(name = "adr_val_start")),
        @AttributeOverride(name = "validityPeriod.endDate", column = @Column(name = "adr_val_end")),
        @AttributeOverride(name = "class1Certification.isAcquired", column = @Column(name = "adr_c1_acq")),
        @AttributeOverride(name = "class1Certification.validityPeriod.startDate", column = @Column(name = "adr_c1_val_start")),
        @AttributeOverride(name = "class1Certification.validityPeriod.endDate", column = @Column(name = "adr_c1_val_end")),
        @AttributeOverride(name = "class2Certification.isAcquired", column = @Column(name = "adr_c2_acq")),
        @AttributeOverride(name = "class2Certification.validityPeriod.startDate", column = @Column(name = "adr_c2_val_start")),
        @AttributeOverride(name = "class2Certification.validityPeriod.endDate", column = @Column(name = "adr_c2_val_end")),
        @AttributeOverride(name = "class3Certification.isAcquired", column = @Column(name = "adr_c3_acq")),
        @AttributeOverride(name = "class3Certification.validityPeriod.startDate", column = @Column(name = "adr_c3_val_start")),
        @AttributeOverride(name = "class3Certification.validityPeriod.endDate", column = @Column(name = "adr_c3_val_end")),
        @AttributeOverride(name = "class4Certification.isAcquired", column = @Column(name = "adr_c4_acq")),
        @AttributeOverride(name = "class4Certification.validityPeriod.startDate", column = @Column(name = "adr_c4_val_start")),
        @AttributeOverride(name = "class4Certification.validityPeriod.endDate", column = @Column(name = "adr_c4_val_end")),
        @AttributeOverride(name = "class5Certification.isAcquired", column = @Column(name = "adr_c5_acq")),
        @AttributeOverride(name = "class5Certification.validityPeriod.startDate", column = @Column(name = "adr_c5_val_start")),
        @AttributeOverride(name = "class5Certification.validityPeriod.endDate", column = @Column(name = "adr_c5_val_end")),
        @AttributeOverride(name = "class6Certification.isAcquired", column = @Column(name = "adr_c6_acq")),
        @AttributeOverride(name = "class6Certification.validityPeriod.startDate", column = @Column(name = "adr_c6_val_start")),
        @AttributeOverride(name = "class6Certification.validityPeriod.endDate", column = @Column(name = "adr_c6_val_end")),
        @AttributeOverride(name = "class7Certification.isAcquired", column = @Column(name = "adr_c7_acq")),
        @AttributeOverride(name = "class7Certification.validityPeriod.startDate", column = @Column(name = "adr_c7_val_start")),
        @AttributeOverride(name = "class7Certification.validityPeriod.endDate", column = @Column(name = "adr_c7_val_end")),
        @AttributeOverride(name = "class8Certification.isAcquired", column = @Column(name = "adr_c8_acq")),
        @AttributeOverride(name = "class8Certification.validityPeriod.startDate", column = @Column(name = "adr_c8_val_start")),
        @AttributeOverride(name = "class8Certification.validityPeriod.endDate", column = @Column(name = "adr_c8_val_end")),
        @AttributeOverride(name = "class9Certification.isAcquired", column = @Column(name = "adr_c9_acq")),
        @AttributeOverride(name = "class9Certification.validityPeriod.startDate", column = @Column(name = "adr_c9_val_start")),
        @AttributeOverride(name = "class9Certification.validityPeriod.endDate", column = @Column(name = "adr_c9_val_end"))
    })
    private Adr adr;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "startDate", column = @Column(name = "med_cert_start")),
        @AttributeOverride(name = "endDate", column = @Column(name = "med_cert_end"))
    })
    private Period medicalCertification;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "isAcquired", column = @Column(name = "psych_acq")),
        @AttributeOverride(name = "validityPeriod.startDate", column = @Column(name = "psych_val_start")),
        @AttributeOverride(name = "validityPeriod.endDate", column = @Column(name = "psych_val_end"))
    })
    private Certification psychotestCertification;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "isAcquired", column = @Column(name = "chem_acq")),
        @AttributeOverride(name = "validityPeriod.startDate", column = @Column(name = "chem_val_start")),
        @AttributeOverride(name = "validityPeriod.endDate", column = @Column(name = "chem_val_end"))
    })
    private Certification chemicalCertification;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "isAcquired", column = @Column(name = "temp_acq")),
        @AttributeOverride(name = "validityPeriod.startDate", column = @Column(name = "temp_val_start")),
        @AttributeOverride(name = "validityPeriod.endDate", column = @Column(name = "temp_val_end"))
    })
    private Certification temperatureCertification;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "isAcquired", column = @Column(name = "san_acq")),
        @AttributeOverride(name = "validityPeriod.startDate", column = @Column(name = "san_val_start")),
        @AttributeOverride(name = "validityPeriod.endDate", column = @Column(name = "san_val_end"))
    })
    private Certification sanepidCertification;
}
