package com.tui.transport.services;

import com.tui.transport.models.ResetToken;
import com.tui.transport.models.Token;
import com.tui.transport.models.User;
import com.tui.transport.repositories.ResetTokenRepository;
import com.tui.transport.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final EmailService emailService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Async
    @Scheduled(fixedRate = 60*1000) // every minute
    public void cleanUpExpiredTokens() {
        List<Token> tokensToDelete = tokenRepository.findAll().stream()
                .filter(token -> token.getExpiryDate().isBefore(LocalDateTime.now()))
                .filter(token -> token.getResetKey() != null) // those are "remember me" tokens
                .toList();
        tokenRepository.deleteAll(tokensToDelete);

        List<Token> incorrectTokens = tokenRepository.findAll().stream()
                .filter(token -> token.getUser() == null)
                .toList();
        tokenRepository.deleteAll(incorrectTokens);

        List<ResetToken> resetTokensToDelete = resetTokenRepository.findAll().stream()
                .filter(token -> token.getExpiryDate().isBefore(LocalDateTime.now()))
                .toList();
        resetTokenRepository.deleteAll(resetTokensToDelete);
        logger.info("Cleaned up {} expired tokens, {} incorrect tokens and {} expired reset tokens", tokensToDelete.size(), incorrectTokens.size(), resetTokensToDelete.size());
    }

    public Token generateNewToken(User user, boolean rememberMe) {
        Token token = new Token();
        token.setUser(user);
        token.setResetKey(rememberMe ? UUID.randomUUID().toString() : null);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        return tokenRepository.save(token);
    }

    public boolean validateToken(String tokenId) {
        Token token = tokenRepository.findById(tokenId).orElse(null);
        return token != null && token.getExpiryDate().isAfter(LocalDateTime.now());
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
