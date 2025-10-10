package com.tui.transport.services;

import com.tui.transport.converters.UserToUserDtoConverter;
import com.tui.transport.dto.UserDto;
import com.tui.transport.dto.UserLoginDto;
import com.tui.transport.dto.UserResetPasswordDto;
import com.tui.transport.models.User;
import com.tui.transport.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final UserRepository userRepository;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final TokenService tokenService;
    public String hashPassword(String password) {
        // Simple hashing for demonstration purposes
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public UserDto login(UserLoginDto userLoginDto) {
        userLoginDto.setPassword(this.hashPassword(userLoginDto.getPassword()));
        User user;
        if (userLoginDto.getEmail() != null) {
            user = userRepository.findByUsername(userLoginDto.getEmail()).orElse(null);
        } else {
            user = userRepository.findByEmail(userLoginDto.getUsername()).orElse(null);
        }
        if (user == null || !user.getPassword().equals(userLoginDto.getPassword())) {
            return null;
        }
        return userToUserDtoConverter.convert(user);
    }

    public boolean sendResetCode(String email) {
        User user = userRepository.findByUsername(email).orElse(null);
        return user != null && tokenService.generateResetToken(user);
    }

    public UserDto resetPassword(UserResetPasswordDto userResetPasswordDto) {
        if (userResetPasswordDto.getEmail() == null || userResetPasswordDto.getNewPassword() == null || userResetPasswordDto.getToken() == null) {
            return null;
        }
        if (!tokenService.validateResetToken(userResetPasswordDto.getEmail(), userResetPasswordDto.getToken())) {
            return null;
        }
        User user = userRepository.findByUsername(userResetPasswordDto.getEmail()).orElse(null);
        if (user == null) {
            return null;
        }
        user.setPassword(this.hashPassword(userResetPasswordDto.getNewPassword()));
        userRepository.save(user);
        return userToUserDtoConverter.convert(user);
    }

    public boolean logout(String email, String userToken) {
        if (tokenService.validateToken(userToken, email)) {
            tokenService.invalidateToken(userToken);
            return true;
        } else {
            return false;
        }
    }

    public void validateToken(String userToken) {
        if(!tokenService.validateToken(userToken)) throw new RuntimeException("Invalid token");
    }
}
