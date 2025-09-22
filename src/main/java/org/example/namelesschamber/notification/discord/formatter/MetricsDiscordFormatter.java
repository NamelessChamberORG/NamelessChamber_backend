package org.example.namelesschamber.notification.discord.formatter;

import org.example.namelesschamber.metrics.dto.TodayMetricsDto;
import org.example.namelesschamber.notification.discord.dto.DiscordEmbedDto;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class MetricsDiscordFormatter {

    public DiscordEmbedDto toDiscordEmbed(TodayMetricsDto metrics) {
        long posts = metrics.posts();
        long members = metrics.members();
        long anonymous = metrics.anonymous();

        long totalUsers = members + anonymous;
        double ratio = (totalUsers == 0) ? 0.0 : (double) members / totalUsers * 100;

        return new DiscordEmbedDto(
                "📊 오늘의 무명소",
                5814783,
                List.of(
                        new DiscordEmbedDto.Field("", "", false),
                        new DiscordEmbedDto.Field("📝 작성된 글", posts + "개", false),
                        new DiscordEmbedDto.Field("👤 신규 회원가입", members + "명", false),
                        new DiscordEmbedDto.Field("📈 익명 대비 신규 회원 비율", String.format("%.1f%%", ratio), false)
                ),
                new DiscordEmbedDto.Footer("Moomyeongso Metrics", "https://cdn-icons-png.flaticon.com/512/1828/1828640.png"),
                Instant.now().toString()
        );
    }
}
