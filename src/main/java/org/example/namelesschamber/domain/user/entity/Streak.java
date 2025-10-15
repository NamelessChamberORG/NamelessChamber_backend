package org.example.namelesschamber.domain.user.entity;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
@ToString
public class Streak {
    private int current;          // 현재 연속일수
    private int best;             // 최고 연속기록
    private String lastSeenDate;  // "yyyy-MM-dd" (KST)
    private boolean todayMarked;  // 오늘 처리 여부

    public static Streak initForToday(String today) {
        return Streak.builder()
                .current(1)
                .best(1)
                .lastSeenDate(today)
                .todayMarked(true)
                .build();
    }
}