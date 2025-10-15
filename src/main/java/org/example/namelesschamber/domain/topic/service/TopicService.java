package org.example.namelesschamber.domain.topic.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.domain.topic.dto.response.TopicResponseDto;
import org.example.namelesschamber.domain.topic.entity.TopicStatus;
import org.example.namelesschamber.domain.topic.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import static org.example.namelesschamber.common.util.TimeUtils.KST;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicResponseDto getTodayTopic() {
        LocalDate today = LocalDate.now(KST);
        return topicRepository.findTopByStatusAndPublishedDateOrderByIdDesc(TopicStatus.PUBLISHED, today)
                .map(TopicResponseDto::from)
                .orElseThrow(() -> new CustomException(ErrorCode.TOPIC_NOT_FOUND));
    }
}