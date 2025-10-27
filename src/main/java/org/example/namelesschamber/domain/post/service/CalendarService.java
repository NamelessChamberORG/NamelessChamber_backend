package org.example.namelesschamber.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.domain.post.dto.response.PostCreateResponseDto;
import org.example.namelesschamber.domain.post.entity.Post;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final MongoTemplate mongoTemplate;
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    public PostCreateResponseDto.WeeklyCalendarDto computeThisWeek(String userId) {
        // 1) 이번 주 일요일(KST)~다음 주 일요일(KST) 경계 계산
        LocalDate weekStartDate = toSunday(LocalDate.now(KST));         // 일요일
        Instant fromUtc = weekStartDate.atStartOfDay(KST).toInstant();
        Instant toUtc   = weekStartDate.plusDays(7).atStartOfDay(KST).toInstant();

        Query q = Query.query(
                Criteria.where("userId").is(userId)
                        .and("createdAt").
                        gte(fromUtc).lt(toUtc)
        );

        List<Post> posts = mongoTemplate.find(q, Post.class);

        int[] counts = new int[7];

        for (Post post : posts) {
            LocalDate kstDate = post.getCreatedAt().atZone(ZoneOffset.UTC)
                    .withZoneSameInstant(KST)
                    .toLocalDate();
            int dayIndex = kstDate.getDayOfWeek().getValue() % 7; // 일=0, 월=1..토=6
            counts[dayIndex]++;
        }

        List<Boolean> days = new ArrayList<>(7);
        List<Integer> countList = new ArrayList<>(7);

        for (int c : counts) { days.add(c > 0); countList.add(c); }

        return new PostCreateResponseDto.WeeklyCalendarDto(
                weekStartDate,
                days,
                countList
        );
    }

    private LocalDate toSunday(LocalDate baseKst) {
        int iso = baseKst.getDayOfWeek().getValue(); // Mon=1..Sun=7
        return baseKst.minusDays(iso % 7);           // Sun 기준 보정
    }
}
