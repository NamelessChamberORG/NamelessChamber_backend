package org.example.namelesschamber.metrics.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.domain.post.repository.PostRepository;
import org.example.namelesschamber.domain.user.entity.UserRole;
import org.example.namelesschamber.domain.user.repository.UserRepository;
import org.example.namelesschamber.metrics.dto.TodayMetricsDto;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public TodayMetricsDto getTodayMetrics() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        long posts = postRepository.countByCreatedAtBetween(start, end);
        long members = userRepository.countByUserRoleAndCreatedAtBetween(UserRole.USER, start, end);
        long anonymous = userRepository.countByUserRoleAndCreatedAtBetween(UserRole.ANONYMOUS, start, end);

        return new TodayMetricsDto(posts, members, anonymous);
    }

    public String buildMetricsReport() {
        TodayMetricsDto metrics = getTodayMetrics();

        long posts = metrics.posts();
        long members = metrics.members();
        long anonymous = metrics.anonymous();

        long totalUsers = members + anonymous;
        double ratio = (totalUsers == 0) ? 0.0 : (double) members / totalUsers * 100;

        return String.format(
                "📊 오늘의 무명소 지표\n- 작성된 고민: %d개\n- 신규 회원가입: %d명\n- 익명 대비 신규 회원 비율: %.1f%%",
                posts, members, ratio
        );
    }

    public Map<String, Object> buildMetricsEmbed() {
        TodayMetricsDto metrics = getTodayMetrics();

        long posts = metrics.posts();
        long members = metrics.members();
        long anonymous = metrics.anonymous();

        long totalUsers = members + anonymous;
        double ratio = (totalUsers == 0) ? 0.0 : (double) members / totalUsers * 100;

        Map<String, Object> embed = Map.of(
                "title", "📊 오늘의 무명소",
                "color", 5814783,
                "fields", List.of(
                        Map.of("name", "", "value", "", "inline", false),
                        Map.of("name", "📝 작성된 글", "value", posts + "개", "inline", false),
                        Map.of("name", "👤 신규 회원가입", "value", members + "명", "inline", false),
                        Map.of("name", "📈 익명 대비 신규 회원 비율", "value", String.format("%.1f%%", ratio), "inline", false)
                ),
                "footer", Map.of(
                        "text", "Moomyeongso Metrics",
                        "icon_url", "https://cdn-icons-png.flaticon.com/512/1828/1828640.png"
                ),
                "timestamp", Instant.now().toString()
        );

        return Map.of("embeds", List.of(embed));
    }
}

