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

    public static PostCreateResponseDto firstOf(String postId, int totalPosts, int coin, WeeklyCalendarDto calendar) {
        return new PostCreateResponseDto(postId, totalPosts, coin, true, calendar);
    }

    public static PostCreateResponseDto nonFirstOf(String postId, int totalPosts, int coin) {
        return new PostCreateResponseDto(postId, totalPosts, coin, false, null);
    }

    public record WeeklyCalendarDto(
            LocalDate weekStart,
            List<Boolean> days,
            List<Integer> counts
    ) {}
}