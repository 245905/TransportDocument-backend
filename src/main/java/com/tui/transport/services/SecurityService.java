package com.tui.transport.services;

import com.tui.transport.converters.UserToUserDtoConverter;
import com.tui.transport.dto.UserDto;
import com.tui.transport.dto.UserLoginDto;
import com.tui.transport.dto.UserResetPasswordDto;
import com.tui.transport.models.Driver;
import com.tui.transport.repositories.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final DriverRepository driverRepository;
    private final UserToUserDtoConverter userToUserDtoConverter;
    public String hashPassword(String password) {
        // Simple hashing for demonstration purposes
        return BCrypt.hashpw(password, "$2a$10$bUL3N6Jn8MxxZc6V9919Le");
    }

    public UserDto login(UserLoginDto userLoginDto) {
        userLoginDto.setPassword(this.hashPassword(userLoginDto.getPassword()));
        Driver driver;
        if (userLoginDto.getEmail() != null) {
            driver = driverRepository.findByEmail(userLoginDto.getEmail()).orElse(null);
        } else {
            driver = driverRepository.findByPhoneNumber(userLoginDto.getPhoneNumber()).orElse(null);
        }
        if (driver == null || !driver.getPasswordHash().equals(userLoginDto.getPassword())) {
            return null;
        }
        return userToUserDtoConverter.convert(driver);
    }

    public boolean sendResetCode(String email) {
        Driver driver = driverRepository.findByPhoneNumber(email).orElse(null);
        return driver != null;
    }

    public UserDto resetPassword(UserResetPasswordDto userResetPasswordDto) {
        if (userResetPasswordDto.getEmail() == null || userResetPasswordDto.getNewPassword() == null || userResetPasswordDto.getToken() == null) {
            return null;
        }
//        if (!tokenService.validateResetToken(userResetPasswordDto.getEmail(), userResetPasswordDto.getToken())) {
//            return null;
//        }
        Driver driver = driverRepository.findByPhoneNumber(userResetPasswordDto.getEmail()).orElse(null);
        if (driver == null) {
            return null;
        }
        driver.setPasswordHash(this.hashPassword(userResetPasswordDto.getNewPassword()));
        driverRepository.save(driver);
        return userToUserDtoConverter.convert(driver);
    }

    public boolean logout(String userToken) {
//        if (tokenService.validateToken(userToken)) {
//            tokenService.invalidateToken(userToken);
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }

    public void validateToken(String userToken) {
//        if(!tokenService.validateToken(userToken)) throw new RuntimeException("Invalid token");
    }

    public Driver getUserFromToken(String token) {
//        return tokenService.getUserFromToken(token);
        return null;
    }

}
