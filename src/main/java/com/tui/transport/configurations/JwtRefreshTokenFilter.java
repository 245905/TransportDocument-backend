package com.tui.transport.configurations;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.tui.transport.models.Driver;
import com.tui.transport.models.ResetToken;
import com.tui.transport.repositories.ResetTokenRepository;
import com.tui.transport.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class JwtRefreshTokenFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final ResetTokenRepository resetTokenRepository;
    private final TokenService tokenService;
    @Qualifier("driverDetailsService")
    private final UserDetailsService driverDetailsService;
    @Qualifier("driverAuthenticationManager")
    private final AuthenticationManager driverAuthenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        try {
            jwtDecoder.decode(jwt);
        } catch (JwtException ex) {
            try {
                JWTClaimsSet claims = JWTParser.parse(jwt).getJWTClaimsSet();
                String username = claims.getSubject();
                String resetTokenValue = request.getHeader("X-Reset-Token");

                JWTClaimsSet resetTokenClaims = JWTParser.parse(resetTokenValue).getJWTClaimsSet();

                Instant tokenExpirationDate = claims.getExpirationTime().toInstant();
                Instant resetTokenExpirationDate = resetTokenClaims.getExpirationTime().toInstant();

                if (tokenExpirationDate.isBefore(Instant.now()) && resetTokenExpirationDate.isAfter(Instant.now())) {
                    ResetToken rt = resetTokenRepository.findById(resetTokenValue).orElseThrow(RuntimeException::new);
                    Driver driver = rt.getDriver();
                    if ((driver.getEmail().equals(username) || driver.getPhoneNumber().equals(username))
                            && rt.getExpiryDate().truncatedTo(ChronoUnit.SECONDS).equals(resetTokenExpirationDate)) {

                        UserDetails driverDetails = this.driverDetailsService.loadUserByUsername(username);
                        String newResetToken = tokenService.generateResetToken(driverDetails.getUsername(),driverDetails.getUsername());
                        response.setHeader("X-Reset-Token", newResetToken);
                        resetTokenRepository.delete(rt);
                        String newAccessToken = tokenService.generateTokenFromUserDetails(driverDetails);
                        response.setHeader("Authorization", "Bearer " + newAccessToken);

                        Authentication auth = driverAuthenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                        driverDetails.getUsername(),
                                        driverDetails.getPassword())
                        );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        request.getSession(false).setAttribute("Authorization", "Bearer " + newAccessToken);
                    }
                }
            } catch (Exception ignored) {
            }
        }
        filterChain.doFilter(request, response);
    }
}
