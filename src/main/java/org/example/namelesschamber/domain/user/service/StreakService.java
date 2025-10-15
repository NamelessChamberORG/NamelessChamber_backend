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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import static org.example.namelesschamber.common.util.TimeUtils.KST;

@Service
@RequiredArgsConstructor
public class StreakService {

    private final MongoTemplate mongoTemplate;

    /**
     * 방문 시 streak을 '하루 한 번만' 갱신하고, 최종 current 값을 반환한다.
     * - 오늘 이미 처리된 경우: no-op 후 현재 current(best 포함)는 DB에서 읽어 반환
     * - 오늘 첫 처리: 어제 방문이면 +1, 아니면 1로 리셋. best는 항상 max 반영
     */
    @Transactional("mongoTransactionManager")
    public void updateOnVisit(User user) {
        LocalDate today = LocalDate.now(KST);
        String todayStr = today.toString();

        //오늘 아직 처리 안 된 유저만 '오늘 처리'로 선점(원자적). 변경 전 스냅샷을 before로 받음
        Query guard = Query.query(Criteria.where("_id").is(user.getId())
                .and("streak.lastSeenDate").ne(todayStr));
        Update markToday = new Update()
                .set("streak.lastSeenDate", todayStr)
                .set("streak.todayMarked", true);
        FindAndModifyOptions opts = FindAndModifyOptions.options()
                .returnNew(false)
                .upsert(false);

        User before = mongoTemplate.findAndModify(guard, markToday, opts, User.class);

        if (before == null) return;

        Streak prev = before.getStreak();
        final int newCurrent;
        final int newBest;

        if (prev == null) {
            newCurrent = 1;
            newBest = 1;
        } else {
            boolean continued = today.minusDays(1).toString().equals(prev.getLastSeenDate());
            newCurrent = continued ? prev.getCurrent() + 1 : 1;
            newBest = Math.max(prev.getBest(), newCurrent);
        }

        Update calc = new Update()
                .set("streak.current", newCurrent)
                .set("streak.best", newBest);
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(user.getId())), calc, User.class);
    }
}