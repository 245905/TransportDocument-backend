package com.tui.transport.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tui.transport.converters.DriverToDriverDtoConverter;
import com.tui.transport.dto.DriverDto;
import com.tui.transport.dto.DriverResetPasswordDto;
import com.tui.transport.dto.LoginDto;
import com.tui.transport.models.Driver;
import com.tui.transport.services.DriverService;
import com.tui.transport.services.SmsService;
import com.tui.transport.services.TokenService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Qualifier("adminAuthenticationManager")
    private final AuthenticationManager adminAuthenticationManager;
    @Qualifier("driverAuthenticationManager")
    private final AuthenticationManager driverAuthenticationManager;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final SmsService smsService;

    private final TokenService tokenService;
    private final DriverService driverService;

    private final DriverToDriverDtoConverter driverToDriverDtoConverter;

    @PostMapping("/login")
    public ResponseEntity<DriverDto> token(@RequestBody LoginDto loginDto) {
        Authentication auth = driverAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail() == null ? loginDto.getPhoneNumber() : loginDto.getEmail(),
                        loginDto.getPassword())
        );

        String token = tokenService.generateToken(auth);
        Driver driver = driverService.getDriverFromAuth(auth);
        DriverDto driverDto = driverToDriverDtoConverter.convert(driver);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer: " + token);

        if (loginDto.isRememberMe()) {
            String resetToken = tokenService.generateResetToken(loginDto.getEmail(), loginDto.getPhoneNumber());
            headers.put("X-Reset-Token", resetToken);
        }

        return ResponseEntity.ok()
                .headers(HttpHeaders.readOnlyHttpHeaders(MultiValueMap.fromSingleValue(headers)))
                .body(driverDto);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<Void> adminLogin(@RequestBody LoginDto loginDto) {
        Authentication auth = adminAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        String token = tokenService.generateToken(auth);
        return ResponseEntity.ok().header("Authorization", "Bearer: " + token).build();
    }

    @PostMapping("/getResetCode")
    public ResponseEntity<Void> getResetCode(@RequestBody DriverResetPasswordDto driverResetPasswordDto) {
        if (driverResetPasswordDto.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }
//        if(!securityService.sendResetCode(driverResetPasswordDto.getEmail())) {
//            return ResponseEntity.status(500).build();
//        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<DriverDto> resetPassword(@RequestBody DriverResetPasswordDto driverResetPasswordDto) {
//        DriverDto userDto = securityService.resetPassword(driverResetPasswordDto);
//        if (userDto == null) {
//            return ResponseEntity.status(401).build();
//        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
//        if(!securityService.logout(token)) {
//            return ResponseEntity.status(401).build();
//        }
        return ResponseEntity.ok().build();
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/admin/test")
    public String adminTest(@RequestHeader(name = "Authorization") String token) {
        logger.info(token.substring(7));
        logger.info("Admin test");
        return "Admin test";
    }

    @RolesAllowed("DRIVER")
    @GetMapping("/test")
    public String userTest() {
        return "User test";
    }


    @PostMapping("/mfa/sendCode")
    public ResponseEntity<Void> sendCode(@RequestBody Map<String, String> body) throws JsonProcessingException {
        String phone = body.get("phone_number");
        smsService.sendCode(phone);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mfa/verifyCode")
    public ResponseEntity<Void> verifyCode(@RequestBody Map<String, String> body) {
        String phone = body.get("phone_number");
        String code = body.get("code");
        boolean result = smsService.verifyCode(phone, code);

        if(result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
