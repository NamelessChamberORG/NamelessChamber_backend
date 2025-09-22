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

import java.util.Map;

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

    @Operation(summary = "디스코드 알림 전송", description = "스케쥴러로 구동되는 디스코드 알림을 수동으로 전송합니다.")
    @PostMapping("/today/notify")
    public ResponseEntity<ApiResponse<Void>> notifyTodayMetrics() {
        Map<String, Object> embed = metricsService.buildMetricsEmbed();
        discordNotifier.sendEmbed(embed);
        return ApiResponse.success(HttpStatus.OK, null);
    }
}
