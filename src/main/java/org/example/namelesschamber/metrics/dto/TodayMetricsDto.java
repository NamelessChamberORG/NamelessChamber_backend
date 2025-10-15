package org.example.namelesschamber.metrics.dto;

public record TodayMetricsDto(
        long shortPosts,
        long shortTotalPosts,
        long longPosts,
        long longTotalPosts,
        long todayPosts,
        long todayTotalPosts,
        long members,
        long anonymous,
        long totalMembers
) {}