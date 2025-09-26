package org.example.namelesschamber.notification.discord.formatter;

import org.example.namelesschamber.metrics.dto.TodayMetricsDto;
import org.example.namelesschamber.notification.discord.dto.DiscordTextDto;
import org.springframework.stereotype.Component;

@Component
public class MetricsDiscordFormatter {

//    public DiscordEmbedDto toDiscordEmbed(TodayMetricsDto metrics) {
//        long posts = metrics.posts();
//        long members = metrics.members();
//        long anonymous = metrics.anonymous();
//
//        long totalUsers = members + anonymous;
//        double ratio = (totalUsers == 0) ? 0.0 : (double) members / totalUsers * 100;
//
//        return new DiscordEmbedDto(
//                "📊 오늘의 무명소",
//                5814783,
//                List.of(
//                        new DiscordEmbedDto.Field("", "", false),
//                        new DiscordEmbedDto.Field("📝 작성된 글", posts + "개", false),
//                        new DiscordEmbedDto.Field("👤 신규 회원가입", members + "명", false),
//                        new DiscordEmbedDto.Field("📈 익명 대비 신규 회원 비율", String.format("%.1f%%", ratio), false)
//                ),
//                new DiscordEmbedDto.Footer("Moomyeongso Metrics", "https://cdn-icons-png.flaticon.com/512/1828/1828640.png"),
//                Instant.now().toString()
//        );
//    }
    public DiscordTextDto toDiscordText(TodayMetricsDto metrics) {
        String content = """
            📊 **오늘의 무명소**
            
            📝 작성된 글
            - 오늘 작성된 짧은 기록 : **%d개**
            - 오늘 작성된 깊은 고민 : **%d개**
    
            👤 신규 회원가입
            - 오늘 회원가입 : **%d명**
    
            📈 익명 대비 신규 회원비율
            - **%.1f%%**
            """.formatted(
                metrics.shortPosts(),
                metrics.longPosts(),
                metrics.members(),
                (metrics.members() + metrics.anonymous()) == 0 ? 0.0 :
                        (double) metrics.members() / (metrics.members() + metrics.anonymous()) * 100
        );

        return new DiscordTextDto(content);
    }
}

