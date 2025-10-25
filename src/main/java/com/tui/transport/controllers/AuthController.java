package com.tui.transport.controllers;

import com.tui.transport.dto.UserDto;
import com.tui.transport.dto.UserLoginDto;
import com.tui.transport.dto.UserResetPasswordDto;
import com.tui.transport.services.SecurityService;
import com.tui.transport.services.TokenService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Qualifier("adminAuthenticationManager")
    private final AuthenticationManager adminAuthenticationManager;
    @Qualifier("driverAuthenticationManager")
    private final AuthenticationManager driverAuthenticationManager;
    private final SecurityService securityService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final TokenService tokenService;

    private final JwtDecoder jwtDecoder;


    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserLoginDto userLoginDto) {
        if(userLoginDto.getPassword() == null || (userLoginDto.getPhoneNumber() == null && userLoginDto.getEmail() == null)) {
            return ResponseEntity.badRequest().build();
        }
        UserDto userDto = securityService.login(userLoginDto);
        if (userDto == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok().body(userDto);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<UserDto> adminLogin(@RequestBody UserLoginDto userLoginDto) {
        Authentication auth = adminAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword())
        );
        String token = tokenService.generateAdminToken(auth);
        return ResponseEntity.ok().header("Authorization", "Bearer: " + token).body(new UserDto());
    }


    @PostMapping("/getResetCode")
    public ResponseEntity<Void> getResetCode(@RequestBody UserResetPasswordDto userResetPasswordDto) {
        if (userResetPasswordDto.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }
        if(!securityService.sendResetCode(userResetPasswordDto.getEmail())) {
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<UserDto> resetPassword(@RequestBody UserResetPasswordDto userResetPasswordDto) {
        UserDto userDto = securityService.resetPassword(userResetPasswordDto);
        if (userDto == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(name = "Authorization") String token) {
        if(!securityService.logout(token)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok().build();
    }

    //example, use this template for login
    @PostMapping("/token")
    public String token(@RequestBody UserLoginDto userLoginDto) {
        Authentication auth = driverAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword())
        );
        logger.info("Token requested for user: {}", auth.getName());
        String token = tokenService.generateToken(auth);
        logger.info("Token generated for user: {}", token);
        return token;
    }

    @PostMapping("/admin/token")
    public String adminToken(@RequestBody UserLoginDto userLoginDto) {
        Authentication auth = adminAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword())
        );
        logger.info("Token requested for admin: {}", auth.getName());
        String token = tokenService.generateAdminToken(auth);
        logger.info("Token generated for admin: {}", token);
        return token;
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/admin/test")
    public String adminTest(@RequestHeader(name = "Authorization") String token) {
        logger.info(token.substring(7));
        logger.info(jwtDecoder.decode(token.substring(7)).getClaims().toString());
        logger.info("Admin test");
        return "Admin test";
    }

    @RolesAllowed("DRIVER")
    @GetMapping("/test")
    public String userTest() {
        logger.info("User test");
        return "User test";
    }

    @GetMapping("/none/test")
    public String noRoleTest() {
        logger.info("No role test");
        return "No role test";
    }
}
