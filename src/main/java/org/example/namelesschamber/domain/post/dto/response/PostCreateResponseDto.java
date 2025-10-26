package org.example.namelesschamber.domain.post.dto.response;

import java.time.LocalDate;
import java.util.List;

public record PostCreateResponseDto(
        String postId,
        int totalPosts,
        int coin,
        boolean showCalendar,
        WeeklyCalendarDto calendar
) {

    public record WeeklyCalendarDto(
            LocalDate weekStart,
            List<Boolean> days,
            List<Integer> counts
    ) {}
}