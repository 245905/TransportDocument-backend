package com.tui.transport.configurations;

import com.tui.transport.models.Admin;
import com.tui.transport.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
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
public class AdminSecurityConfig {

    private final AdminRepository adminRepository;

    @Bean(name = "adminDetailsService")
    public UserDetailsService userDetailsService() {
        return username -> {
            Admin admin = adminRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono admina: " + username));
            return User.withUsername(admin.getUsername())
                    .password(admin.getPasswordHash())
                    .roles("ADMIN")
                    .build();
        };
    }

    @Bean(name = "adminAuthenticationManager")
    public AuthenticationManager adminAuthenticationManager(PasswordEncoder passwordEncoder,
                                                            @Qualifier("adminDetailsService") UserDetailsService adminDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(adminDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
