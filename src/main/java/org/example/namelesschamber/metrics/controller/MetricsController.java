package org.example.namelesschamber.metrics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.util.DiscordNotifier;
import org.example.namelesschamber.metrics.service.MetricsService;
import org.example.namelesschamber.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
@Tag(name = "Metrics", description = "통계 API")
public class MetricsController {

    private final MetricsService metricsService;
    private final DiscordNotifier discordNotifier;


    @Operation(summary = "오늘 Metrics 조회", description = "오늘 작성된 글 수, 회원가입 수, 회원 비율을 JSON으로 응답합니다.")
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<String>> getTodayMetrics() {
        String report = metricsService.buildMetricsReport();
        return ApiResponse.success(HttpStatus.OK, report);
    }

    @PostMapping("/today/notify")
    public ResponseEntity<ApiResponse<Void>> notifyTodayMetrics() {
        String report = metricsService.buildMetricsReport();
        discordNotifier.send(report);
        return ApiResponse.success(HttpStatus.OK, null);
    }
}
