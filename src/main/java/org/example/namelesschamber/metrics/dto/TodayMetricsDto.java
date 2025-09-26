package org.example.namelesschamber.metrics.dto;

public record TodayMetricsDto(
        long shortPosts,
        long longPosts,
        long members,
        long anonymous
) {}