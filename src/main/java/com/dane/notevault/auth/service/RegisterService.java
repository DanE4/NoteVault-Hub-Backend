package com.dane.notevault.auth.service;

import com.dane.notevault.auth.Response;
import com.dane.notevault.auth.email.EmailServiceImpl;
import com.dane.notevault.auth.model.ConfirmationToken;
import com.dane.notevault.auth.request.RegisterRequest;
import com.dane.notevault.auth.request.RegisterResponse;
import com.dane.notevault.repository.ConfirmationTokenRepository;
import com.dane.notevault.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService {
    private final EmailValidator emailValidator;
    private final UserService userService;
    private final EmailServiceImpl emailSender;

    public Response register(RegisterRequest request) {
        log.info("registering");
        try {
            boolean isValidEmail = emailValidator.test(request.email());
            if (!isValidEmail) {
                log.warn("Email not valid");
                return Response.builder()
                        .response("Email not valid")
                        .build();
            }
            RegisterResponse registerResponse = userService.registerUser(request);

            String link = "http://localhost:8080/api/register/confirm?token=" + registerResponse.getConfirmToken();

            //TODO: send email
            //public void sendEmail(String to, String subject, String body) {
            emailSender.sendEmail(request.email(), "Confirm your email", buildEmail("", link));
            return Response.builder()
                    .data(Map.of(
                            "accessToken", registerResponse.getAccesstoken(),
                            "refreshToken", registerResponse.getRefreshtoken()
                    ))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.builder()
                    .response("Internal server error")
                    .build();
        }
    }

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        log.info("Getting token");
        var asd = confirmationTokenRepository.findByToken(token);
        log.info(asd.toString());
        return confirmationTokenRepository.findByToken(token);
    }

    public Response confirmToken(String token) {
        ConfirmationToken confirmationToken = getToken(token).orElseThrow(() -> new IllegalStateException("Token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            log.error("Email already confirmed");
            throw new IllegalStateException("Email already confirmed");
        }
        ZonedDateTime expiresAt = confirmationToken.getExpiresAt().atZone(ZoneId.systemDefault());
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());

        if (expiresAt.isBefore(now)) {
            log.error("Token already expired");
            throw new IllegalStateException("Token already expired");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
        userService.enableAppUser(confirmationToken.getUser().getEmail());
        return Response.builder()
                .response("Email confirmed")
                .build();
    }

    public String buildEmail(String name, String link) throws IOException {
        try {
            // Read the HTML content
            Resource htmlResource = new ClassPathResource("email_confirmation.html");
            byte[] htmlContent = FileCopyUtils.copyToByteArray(htmlResource.getInputStream());
            String emailBody = new String(htmlContent, StandardCharsets.UTF_8);

            // Read the CSS content
            Resource cssResource = new ClassPathResource("email.css");
            byte[] cssContent = FileCopyUtils.copyToByteArray(cssResource.getInputStream());
            String cssStyles = new String(cssContent, StandardCharsets.UTF_8);

            // Set the style tag in the HTML content with the CSS content
            emailBody = emailBody.replace("</head>", "<style>" + cssStyles + "</style></head>");

            // Replace the "REPLACETHIS" placeholder with the given email
            emailBody = emailBody.replace("REPLACETHIS", link);

            // Use the modified emailBody as the body of the email
            log.info("Email built");
            return emailBody;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

}
