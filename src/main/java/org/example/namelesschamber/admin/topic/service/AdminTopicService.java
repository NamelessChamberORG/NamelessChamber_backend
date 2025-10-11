package org.example.namelesschamber.admin.topic.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.domain.topic.dto.request.TopicRequestDto;
import org.example.namelesschamber.domain.topic.entity.Topic;
import org.example.namelesschamber.domain.topic.entity.TopicStatus;
import org.example.namelesschamber.domain.topic.repository.TopicRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminTopicService {

    private final TopicRepository topicRepository;

    public void createTopic(TopicRequestDto dto) {
        Topic topic = Topic.builder()
                .title(dto.title())
                .subtitle(dto.subtitle())
                .status(TopicStatus.READY)
                .build();

        topicRepository.save(topic);
    }
}