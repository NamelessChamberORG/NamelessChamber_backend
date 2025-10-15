package org.example.namelesschamber.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.domain.user.entity.Streak;
import org.example.namelesschamber.domain.user.entity.User;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class StreakService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final MongoTemplate mongoTemplate;

    /**
     * 방문 시 streak을 '하루 한 번만' 갱신하고, 최종 current 값을 반환한다.
     * - 오늘 이미 처리된 경우: no-op 후 현재 current(best 포함)는 DB에서 읽어 반환
     * - 오늘 첫 처리: 어제 방문이면 +1, 아니면 1로 리셋. best는 항상 max 반영
     */
    public void updateOnVisitAndGetCurrent(User user) {
        LocalDate today = LocalDate.now(KST);
        String todayStr = today.toString();

        // 1) 오늘 아직 처리 안 된 유저만 '오늘 처리'로 선점(원자적). 변경 전 스냅샷을 before로 받음
        Query guard = Query.query(Criteria.where("_id").is(user.getId())
                .and("streak.lastSeenDate").ne(todayStr));
        Update markToday = new Update()
                .set("streak.lastSeenDate", todayStr)
                .set("streak.todayMarked", true);
        FindAndModifyOptions opts = FindAndModifyOptions.options()
                .returnNew(false)  // 변경 '전' 문서 반환
                .upsert(false);

        User before = mongoTemplate.findAndModify(guard, markToday, opts, User.class);

        // 1-1) 이미 오늘 처리됨 → 아무 것도 하지 않음
        if (before == null) return;

        // 2) 오늘 '처음' 처리된 경우 → 이전 상태 기반 계산
        Streak prev = before.getStreak();
        if (prev == null) {
            // 극초기 방어: streak 없던 사용자
            initStreak(user.getId(), todayStr);
            return;
        }

        boolean continued = today.minusDays(1).toString().equals(prev.getLastSeenDate());
        int newCurrent = continued ? prev.getCurrent() + 1 : 1;
        int newBest = Math.max(prev.getBest(), newCurrent);

        Update calc = new Update()
                .set("streak.current", newCurrent)
                .set("streak.best", newBest);
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(user.getId())), calc, User.class);
    }

    private void initStreak(String userId, String todayStr) {
        Update init = new Update()
                .set("streak.current", 1)
                .set("streak.best", 1)
                .set("streak.lastSeenDate", todayStr)
                .set("streak.todayMarked", true);
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(userId)), init, User.class);
    }
}