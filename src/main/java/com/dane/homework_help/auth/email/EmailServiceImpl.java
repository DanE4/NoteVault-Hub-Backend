package com.dane.homework_help.auth.email;

import com.resend.Resend;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(String to, String subject, String body) {
        log.info("Sending email");
        //api key from env
        String apiKey = System.getenv("RESEND_API_KEY");
        Resend resend = new Resend(apiKey);
        String from = System.getenv("RESEND_FROM");
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .html("<p>Hello <strong>Hello</strong>!</p>")
                .build();
        SendEmailResponse data = resend.emails().send(sendEmailRequest);
        log.info("Email sent");
        log.info(data.toString());
        log.info(data.getId());
    }
}