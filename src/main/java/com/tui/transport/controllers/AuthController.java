package com.tui.transport.controllers;

import com.tui.transport.dto.UserDto;
import com.tui.transport.dto.UserLoginDto;
import com.tui.transport.dto.UserResetPasswordDto;
import com.tui.transport.repositories.UserRepository;
import com.tui.transport.services.SecurityService;
import com.tui.transport.services.TokenService;
import com.tui.transport.Utils.CheckToken.CheckToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
@RequiredArgsConstructor()
public class AuthController {

    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserLoginDto userLoginDto) {
        if(userLoginDto.getPassword() == null || (userLoginDto.getUsername() == null && userLoginDto.getEmail() == null)) {
            return ResponseEntity.badRequest().build();
        }
        UserDto userDto = securityService.login(userLoginDto);
        if (userDto == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok().body(userDto);
    }

    @PostMapping("/getResetCode")
    public ResponseEntity<Void> getResetCode(@RequestBody UserResetPasswordDto userResetPasswordDto) {
        if (userResetPasswordDto.getEmail() == null) {
            return ResponseEntity.badRequest().build();
        }
        if(!securityService.sendResetCode(userResetPasswordDto.getEmail())) {
            return ResponseEntity.status(500).build();
        }
        // For security reasons, we always return 200 OK even if the user does not exist
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

    @CheckToken
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(name = "Authorization") String token, @RequestBody String email) {
        if(!securityService.logout(email, token)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok().build();
    }
}
