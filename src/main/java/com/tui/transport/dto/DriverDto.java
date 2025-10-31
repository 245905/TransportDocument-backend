package com.tui.transport.dto;

import com.tui.transport.models.DriverQualifications;
import com.tui.transport.models.Truck;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DriverDto {
    private String name;
    private String surname;

    private String birthPlace;
    private LocalDate dateOfBirth;

    private String country;

    private String phoneNumber;
    private String email;

    private String language = "english";

    private DriverQualifications driverQualifications;

    private Truck currentTruck;
}
