package org.example.namelesschamber.notification.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.namelesschamber.metrics.dto.TodayMetricsDto;
import org.example.namelesschamber.metrics.service.MetricsService;
import org.example.namelesschamber.notification.discord.dto.DiscordEmbedDto;
import org.example.namelesschamber.notification.discord.dto.DiscordTextDto;
import org.example.namelesschamber.notification.discord.formatter.MetricsDiscordFormatter;
import org.example.namelesschamber.notification.discord.service.DiscordNotifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final MetricsService metricsService;
    private final MetricsDiscordFormatter metricsDiscordFormatter;
    private final DiscordNotifier discordNotifier;

//    @Scheduled(cron = "0 0 23 * * *")
//    public void sendDailyMetricsReport() {
//        TodayMetricsDto metrics = metricsService.getTodayMetrics();
//        DiscordEmbedDto embed = metricsDiscordFormatter.toDiscordEmbed(metrics);
//        discordNotifier.sendEmbed(embed);
//        log.info("Metrics report sent to Discord: {}", embed);
//    }

    @Scheduled(cron = "0 0 23 * * *")
    public void sendDailyMetricsReportText() {
        TodayMetricsDto metrics = metricsService.getTodayMetrics();
        DiscordTextDto texts = metricsDiscordFormatter.toDiscordText(metrics);
        discordNotifier.sendText(texts);
        log.info("Metrics report sent to Discord: {}", texts);
    }
}
