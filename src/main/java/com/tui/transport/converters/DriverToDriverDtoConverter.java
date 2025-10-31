package com.tui.transport.converters;

import com.tui.transport.dto.DriverDto;
import com.tui.transport.models.Driver;
import com.tui.transport.utils.Converter;
import org.springframework.stereotype.Service;

@Service
public class DriverToDriverDtoConverter extends Converter<DriverDto, Driver> {

    public DriverToDriverDtoConverter() {
        super(DriverDto.class);
    }

    public DriverDto convert(Driver driver) {
        if (driver == null) {
            return null;
        }
        return super.convert(driver);
    }
}
