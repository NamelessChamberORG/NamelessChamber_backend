package org.example.namelesschamber.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class DiscordNotifier {

    private final RestTemplate restTemplate;
    private final String webhookUrl;

    public DiscordNotifier(RestTemplate restTemplate,
                           @Value("${discord.webhook.url}") String webhookUrl) {
        this.restTemplate = restTemplate;
        this.webhookUrl = webhookUrl;
    }

    @Async
    public void send(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("content", message);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        int maxAttempts = 3;
        int attempt = 0;

        while (attempt < maxAttempts) {
            try {
                attempt++;
                restTemplate.postForEntity(webhookUrl, request, Void.class);
                log.info("Discord 알림 전송 성공 (시도 {}회)", attempt);
                return;
            } catch (RestClientException e) {
                log.error("Discord 알림 전송 실패 (시도 {}회)", attempt, e);

                if (attempt >= maxAttempts) {
                    log.error("Discord 알림 전송 최종 실패)");
                    break;
                }
                try {
                    Thread.sleep(2000L * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.warn("재시도 대기 중 인터럽트 발생");
                    break;
                }
            }
        }
    }
}
