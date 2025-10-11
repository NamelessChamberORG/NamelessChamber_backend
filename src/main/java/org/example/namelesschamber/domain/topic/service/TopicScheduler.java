package org.example.namelesschamber.domain.topic.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.domain.topic.entity.Topic;
import org.example.namelesschamber.domain.topic.entity.TopicStatus;
import org.example.namelesschamber.domain.topic.repository.TopicRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TopicScheduler {

    private final TopicRepository topicRepository;
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    /**
     * 매일 자정(한국시간) 기준으로 다음 READY 주제를 발행.
     * 모두 소비되면 다시 RESET 후 처음부터 순환.
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void publishNextTopic() {
        LocalDate today = LocalDate.now(KST);

        //어제까지 PUBLISHED → READY
        List<Topic> oldPublished = topicRepository
                .findAllByStatusAndPublishedDateBefore(TopicStatus.PUBLISHED, today);
        if (!oldPublished.isEmpty()) {
            oldPublished.forEach(t -> t.setStatus(TopicStatus.READY));
            topicRepository.saveAll(oldPublished);
            log.debug("[TopicScheduler] {}건의 과거 발행 항목을 READY로 되돌림", oldPublished.size());
        }

        //최신 1개만 PUBLISHED 유지, 나머지 READY로 되돌림
        List<Topic> publishedToday = topicRepository.findAllByStatusAndPublishedDate(TopicStatus.PUBLISHED, today);
        if (!publishedToday.isEmpty()) {
            // ObjectId ASC 정렬 → 마지막이 최신
            publishedToday.sort(Comparator.comparing(Topic::getPublishedDate).thenComparing(Topic::getId));
            if (publishedToday.size() > 1) {
                for (int i = 0; i < publishedToday.size() - 1; i++) {
                    Topic dup = publishedToday.get(i);
                    dup.setStatus(TopicStatus.READY);
                }
                topicRepository.saveAll(publishedToday.subList(0, publishedToday.size() - 1));
                log.warn("[TopicScheduler] 오늘자 중복 PUBLISHED {}건 발견 → 최신 1개만 유지, {}개 READY로 되돌림",
                        publishedToday.size(), publishedToday.size() - 1);
            }
            log.info("[TopicScheduler] {} 주제는 이미 발행되었습니다.", today);
            return;
        }

        // 최근 발행 기준으로 다음 READY 선택
        Topic lastPublished = topicRepository
                .findTopByStatusOrderByIdDesc(TopicStatus.PUBLISHED) // 최신 발행
                .orElse(null);

        Topic next = (lastPublished == null)
                ? topicRepository.findFirstByStatusOrderByIdAsc(TopicStatus.READY)
                .orElseGet(this::resetAndFetchFirst)
                : topicRepository.findFirstByStatusAndIdGreaterThanOrderByIdAsc(
                        TopicStatus.READY, lastPublished.getId())
                .orElseGet(this::resetAndFetchFirst);

        next.publish(today);
        topicRepository.save(next);
        log.info("[TopicScheduler] 오늘의 주제 발행 완료: {}", next.getTitle());
    }

    /**
     * 모든 주제를 READY로 초기화하고 첫 번째 주제를 반환
     */
    private Topic resetAndFetchFirst() {
        List<Topic> all = topicRepository.findAll();
        all.forEach(Topic::reset);
        topicRepository.saveAll(all);

        return topicRepository.findFirstByStatusOrderByIdAsc(TopicStatus.READY)
                .orElseThrow(() -> new CustomException(ErrorCode.TOPIC_NOT_FOUND));
    }
}