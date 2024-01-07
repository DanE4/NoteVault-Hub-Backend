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

        //String apiKey = System.getenv("RESEND_API_KEY");
        String apiKey = "re_dgiNwf4u_35rWgy6YGQCNhC7837RLnVj9";
        Resend resend = new Resend(apiKey);
        String from = "noreply@danieleros.xyz";
        //String from = System.getenv("RESEND_FROM");

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from(from)
                .to(to)
                .subject(subject)
                .html(body)
                .build();
        SendEmailResponse data = resend.emails().send(sendEmailRequest);
        log.info("Email sent");
        log.info(data.toString());
        log.info(data.getId());
    }
}