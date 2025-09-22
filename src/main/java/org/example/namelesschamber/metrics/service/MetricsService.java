package org.example.namelesschamber.metrics.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.domain.post.repository.PostRepository;
import org.example.namelesschamber.domain.user.entity.UserRole;
import org.example.namelesschamber.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 오늘 작성된 글 개수
     */
    public long countTodayPosts() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        return postRepository.countByCreatedAtBetween(start, end);
    }

    /**
     * 오늘 회원가입한 사용자 수
     */
    public long countTodaySignups() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();
        return userRepository.countByUserRoleAndCreatedAtBetween(UserRole.USER, start, end);
    }

    /**
     * 오늘 익명 → 회원 전환율
     */
    public double calculateTodayUserRatio() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay();

        long anonymous = userRepository.countByUserRoleAndCreatedAtBetween(UserRole.ANONYMOUS, start, end);
        long member = userRepository.countByUserRoleAndCreatedAtBetween(UserRole.USER, start, end);

        long total = anonymous + member;
        if (total == 0) return 0.0;

        return (double) member / total * 100;
    }

    /**
     * Discord 전송용 문자열 포맷
     */
    public String buildMetricsReport() {
        long posts = countTodayPosts();
        long signups = countTodaySignups();
        double userRatio = calculateTodayUserRatio();

        return String.format(
                "📊 오늘의 무명소 지표\n- 오늘 작성된 글: %d개\n- 오늘 신규 회원가입: %d명\n- 익명 대비 신규 회원 비율: %.1f%%",
                posts, signups, userRatio
        );
    }
}
