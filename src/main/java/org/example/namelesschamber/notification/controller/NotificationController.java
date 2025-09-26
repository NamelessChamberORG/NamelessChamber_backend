package org.example.namelesschamber.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.metrics.dto.TodayMetricsDto;
import org.example.namelesschamber.metrics.service.MetricsService;
import org.example.namelesschamber.notification.discord.dto.DiscordTextDto;
import org.example.namelesschamber.notification.discord.formatter.MetricsDiscordFormatter;
import org.example.namelesschamber.notification.discord.service.DiscordNotifier;
import org.example.namelesschamber.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "알림 API")
public class NotificationController {

    private final MetricsService metricsService;
    private final MetricsDiscordFormatter metricsDiscordFormatter;
    private final DiscordNotifier discordNotifier;

    @Operation(summary = "오늘 Metrics 디스코드 알림 전송", description = "스케쥴러로 구동되는 디스코드 알림을 수동으로 전송합니다.")
    @PostMapping("/metrics/today")
    public ResponseEntity<ApiResponse<Void>> notifyTodayMetrics() {
        TodayMetricsDto metrics = metricsService.getTodayMetrics();
        DiscordTextDto texts = metricsDiscordFormatter.toDiscordText(metrics);
        discordNotifier.sendText(texts);
        return ApiResponse.success(HttpStatus.OK);
    }
}
