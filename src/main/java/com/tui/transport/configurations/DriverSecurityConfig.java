package com.tui.transport.configurations;

import com.tui.transport.models.Driver;
import com.tui.transport.repositories.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DriverSecurityConfig {
    private final DriverRepository driverRepository;

    private final Logger logger = LoggerFactory.getLogger(DriverSecurityConfig.class);


    @Bean(name = "driverDetailsService")
    public UserDetailsService userDetailsService() {
        return username -> {
            logger.debug("Szukanie kierowcy: {}", username);
            Driver driver = driverRepository.findByEmail(username)
                    .or(() -> driverRepository.findByPhoneNumber(username))
                    .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika: " + username));
            String principal = driver.getEmail() != null ? driver.getEmail() : driver.getPhoneNumber();
            return User.withUsername(principal)
                    .password(driver.getPasswordHash())
                    .roles("USER")
                    .build();
        };
    }

    @Bean(name = "driverAuthenticationManager")
    public AuthenticationManager driverAuthenticationManager(PasswordEncoder passwordEncoder,
                                                             @Qualifier("driverDetailsService") UserDetailsService driverDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(driverDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
