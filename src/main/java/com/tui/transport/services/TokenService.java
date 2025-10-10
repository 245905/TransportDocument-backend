package com.tui.transport.services;

import com.tui.transport.models.ResetToken;
import com.tui.transport.models.Token;
import com.tui.transport.models.User;
import com.tui.transport.repositories.ResetTokenRepository;
import com.tui.transport.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final EmailService emailService;

    @Async
    @Scheduled(fixedRate = 60*1000) // every minute
    public void cleanUpExpiredTokens() {
        tokenRepository.deleteAll(tokenRepository.findAll().stream()
                .filter(token -> token.getExpiryDate().isBefore(LocalDateTime.now()))
                .filter(token -> !token.getRepeating())
                .toList());
        resetTokenRepository.deleteAll(resetTokenRepository.findAll().stream()
                .filter(token -> token.getExpiryDate().isBefore(LocalDateTime.now()))
                .toList());
    }

    public Token generateNewToken(User user, boolean repeating) {
        Token token = new Token();
        token.setUser(user);
        token.setRepeating(repeating);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        return tokenRepository.save(token);
    }

    public boolean validateToken(String tokenId) {
        Token token = tokenRepository.findById(tokenId).orElse(null);
        return token != null && token.getExpiryDate().isAfter(LocalDateTime.now());
    }

    public boolean validateToken(String tokenId, String email) {
        Token token = tokenRepository.findById(tokenId).orElse(null);
        return token != null && token.getExpiryDate().isAfter(LocalDateTime.now()) && token.getUser().getEmail().equals(email);
    }

    public void invalidateToken(String tokenId) {
        tokenRepository.deleteById(tokenId);
    }

    public boolean generateResetToken(User user) {
        ResetToken resetToken = new ResetToken();
        resetToken.setUser(user);
        int code = (int)(Math.random() * 900000) + 100000; // 6-digit code
        resetToken.setToken(String.valueOf(code));
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        resetToken = resetTokenRepository.save(resetToken);
        return emailService.sendResetEmail(user.getUsername(), resetToken.getToken());
    }

    public boolean validateResetToken(String email, String tokenStr) {
        ResetToken token = resetTokenRepository.findById(tokenStr).orElse(null);
        return token != null && token.getUser().getEmail().equals(email) && token.getExpiryDate().isAfter(LocalDateTime.now());
    }
}
