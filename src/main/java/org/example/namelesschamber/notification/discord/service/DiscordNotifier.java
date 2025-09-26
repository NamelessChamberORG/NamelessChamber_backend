package org.example.namelesschamber.notification.discord.service;

import lombok.extern.slf4j.Slf4j;
import org.example.namelesschamber.notification.discord.dto.DiscordTextDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class DiscordNotifier {

    private static final int MAX_ATTEMPTS = 3;

    private final RestTemplate restTemplate;
    private final String webhookUrl;

    public DiscordNotifier(RestTemplate restTemplate,
                           @Value("${discord.webhook.url}") String webhookUrl) {
        this.restTemplate = restTemplate;
        this.webhookUrl = webhookUrl;
    }

    public void sendText(DiscordTextDto dto) {
        Map<String, Object> body = Map.of("content", dto.content());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                restTemplate.postForEntity(webhookUrl, request, Void.class);
                log.info("Discord Text 알림 전송 성공 (시도 {}회)", attempt);
                return;
            } catch (RestClientException e) {
                log.error("Discord Text 알림 전송 실패 (시도 {}회)", attempt, e);

                if (attempt == MAX_ATTEMPTS) {
                    log.error("Discord Text 알림 최종 실패 (총 {}회 시도)", attempt);
                    break;
                }

                try {
                    Thread.sleep(1000L * attempt); // 1초, 2초 backoff
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.warn("재시도 대기 중 인터럽트 발생");
                    break;
                }
            }
        }
    }


//    @Async
//    public void sendEmbed(DiscordEmbedDto embedDto) {
//        Map<String, Object> body = Map.of("embeds", java.util.List.of(embedDto));
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//
//        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
//            try {
//                restTemplate.postForEntity(webhookUrl, request, Void.class);
//                log.info("Discord Embed 알림 전송 성공 (시도 {}회)", attempt);
//                return;
//            } catch (RestClientException e) {
//                log.error("Discord Embed 알림 전송 실패 (시도 {}회)", attempt, e);
//
//                if (attempt == MAX_ATTEMPTS) {
//                    log.error("Discord Embed 알림 최종 실패 (총 {}회 시도)", attempt);
//                    break;
//                }
//
//                try {
//                    Thread.sleep(1000L * attempt); // 1초, 2초 backoff
//                } catch (InterruptedException ie) {
//                    Thread.currentThread().interrupt();
//                    log.warn("재시도 대기 중 인터럽트 발생");
//                    break;
//                }
//            }
//        }
//    }
}
