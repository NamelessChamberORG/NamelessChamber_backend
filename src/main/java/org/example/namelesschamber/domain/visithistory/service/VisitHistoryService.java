package org.example.namelesschamber.domain.visithistory.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.domain.visithistory.entity.VisitHistory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import static org.example.namelesschamber.common.util.TimeUtils.KST;

@Service
@RequiredArgsConstructor
public class VisitHistoryService {
    private final MongoTemplate mongoTemplate;

    public void recordDailyVisit(String userId) {
        LocalDate today = LocalDate.now(KST);
        String id = userId + "#" + today;
        Query q = Query.query(Criteria.where("_id").is(id));

        //문서가 없을때만 값을 세팅, 있다면 패스 (하루에 한번이라는 멱등성을 보장)
        Update u = new Update()
                .setOnInsert("userId", userId)
                .setOnInsert("date", today.toString())
                .setOnInsert("createdAt", LocalDateTime.now(KST));
        mongoTemplate.upsert(q, u, VisitHistory.class);
    }

    public Set<String> getVisitedDates(String userId, LocalDate from, LocalDate to) {
        var docs = mongoTemplate.find(
                Query.query(Criteria.where("userId").is(userId)
                        .and("date").gte(from.toString()).lte(to.toString())),
                VisitHistory.class
        );
        return docs.stream().map(VisitHistory::getDate).collect(Collectors.toSet());
    }
}