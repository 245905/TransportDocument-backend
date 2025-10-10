package com.tui.transport.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private boolean sendEmail(String to, String subject, String body) {
        //TODO: Implement email sending logic
        return true;
    }

    public boolean sendResetEmail(String to, String token) {
        String subject = "Password Reset Request";
        String body = "Your password reset code is: " + token + "\nThis code will expire in 5 minutes.";
        return sendEmail(to, subject, body);
    }
}
