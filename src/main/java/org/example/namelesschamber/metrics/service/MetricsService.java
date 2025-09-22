package org.example.namelesschamber.metrics.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.domain.post.repository.PostRepository;
import org.example.namelesschamber.domain.user.entity.UserRole;
import org.example.namelesschamber.domain.user.repository.UserRepository;
import org.example.namelesschamber.metrics.dto.TodayMetricsDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
