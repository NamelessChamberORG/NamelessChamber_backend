package org.example.namelesschamber.metrics.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.domain.post.entity.PostType;
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

        long shortPosts = postRepository.countByTypeAndCreatedAtBetween(PostType.SHORT, start, end);
        long shortTotalPosts = postRepository.countByType(PostType.SHORT);
        long longPosts = postRepository.countByTypeAndCreatedAtBetween(PostType.LONG, start, end);
        long longTotalPosts = postRepository.countByType(PostType.LONG);
        long todayPosts = postRepository.countByTypeAndCreatedAtBetween(PostType.TODAY, start, end);
        long todayTotalPosts = postRepository.countByType(PostType.TODAY);
        long members = userRepository.countByUserRoleAndCreatedAtBetween(UserRole.USER, start, end);
        long anonymous = userRepository.countByUserRoleAndCreatedAtBetween(UserRole.ANONYMOUS, start, end);

        return new TodayMetricsDto(shortPosts,shortTotalPosts, longPosts, longTotalPosts,
                todayPosts,todayTotalPosts, members, anonymous);
    }
}
