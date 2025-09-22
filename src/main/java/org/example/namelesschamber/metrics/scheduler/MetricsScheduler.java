package org.example.namelesschamber.metrics.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.namelesschamber.metrics.service.MetricsService;
import org.example.namelesschamber.common.util.DiscordNotifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetricsScheduler {

    private final MetricsService metricsService;
    private final DiscordNotifier discordNotifier;

    @Scheduled(cron = "0 */3 * * * *")
    public void reportMetricsEveryDay() {
        String report = metricsService.buildMetricsReport();
        discordNotifier.send(report);
        log.info("Metrics report sent to Discord: {}", report);
    }
}
