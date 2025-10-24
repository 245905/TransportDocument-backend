package com.tui.transport.models;

import com.tui.transport.models.helpers.Certification;
import com.tui.transport.models.helpers.Period;
import jakarta.persistence.Embeddable;
import lombok.Data;
@Embeddable
@Data
public class Licence {
    private String licenceNumber;
    private String licenceAuthority;
    private Period validityPeriod;

    private Certification B_Certification;
    private Certification C_Certification;
    private Certification CE_Certification;
    private Certification D_Certification;
}
