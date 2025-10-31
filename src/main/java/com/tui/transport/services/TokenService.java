package com.tui.transport.services;

import com.tui.transport.models.Driver;
import com.tui.transport.models.ResetToken;
import com.tui.transport.repositories.DriverRepository;
import com.tui.transport.repositories.ResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final ResetTokenRepository resetTokenRepository;
    private final DriverRepository driverRepository;
    private final EmailService emailService;
    private final JwtEncoder jwtEncoder;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.MINUTES))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateResetToken(String email, String phoneNumber) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS))
                .build();

        String resetToken = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        Driver driver = null;
        if(email != null && !email.isEmpty()) {
            driver = driverRepository.findByEmail(email).orElse(null);
        } else if(phoneNumber != null && !phoneNumber.isEmpty()) {
            driver = driverRepository.findByPhoneNumber(phoneNumber).orElse(null);
        }
        if(driver == null) {
            throw new RuntimeException("Driver not found");
        }

        ResetToken rt = new ResetToken();
        rt.setToken(resetToken);
        rt.setDriver(driver);
        rt.setExpiryDate(now.plus(30, ChronoUnit.DAYS));

        resetTokenRepository.save(rt);
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateTokenFromUserDetails(UserDetails userDetails) {
        Instant now = Instant.now();
        String scope = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(userDetails.getUsername())
                .claim("scope", scope)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
