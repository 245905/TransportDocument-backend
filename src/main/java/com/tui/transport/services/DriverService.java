package com.tui.transport.services;

import com.tui.transport.models.Driver;
import com.tui.transport.repositories.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;

    public Driver getDriverFromAuth(Authentication authentication) {
        String username = authentication.getName();
        return this.getDriverByUsername(username);
    }

    public Driver getDriverByUsername(String username){
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.\\w{2,6}$";
        Driver driver;
        if (username.matches(emailRegex)) {
            driver = driverRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika: " + username));
        } else {
            driver = driverRepository.findByPhoneNumber(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika: " + username));
        }
        return driver;
    }

}
