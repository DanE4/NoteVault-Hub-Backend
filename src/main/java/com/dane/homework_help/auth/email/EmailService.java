package com.dane.homework_help.auth.email;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
