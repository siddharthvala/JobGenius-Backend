package com.jobmatcher.jobmatcher_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${RESEND_API_KEY}")
    private String resendApiKey;

    @Value("${app.mail.from}")
    private String fromAddress;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendHtmlEmail(String toEmail, String subject, String htmlBody) {

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(resendApiKey);

            Map<String, Object> body = Map.of(
                    "from", fromAddress,
                    "to", new String[]{toEmail},
                    "subject", subject,
                    "html", htmlBody
            );

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.resend.com/emails",
                    request,
                    String.class
            );

            log.info("Resend response: {}", response.getBody());

        } catch (Exception e) {
            log.error("Resend email failed: {}", e.getMessage());
        }
    }
}
