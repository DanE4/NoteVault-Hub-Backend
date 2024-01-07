package com.dane.notevault.auth.email;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
