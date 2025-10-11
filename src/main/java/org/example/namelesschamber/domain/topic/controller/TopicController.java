package org.example.namelesschamber.domain.topic.controller;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.domain.topic.dto.response.TopicResponseDto;
import org.example.namelesschamber.domain.topic.service.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping("/today")
    public ResponseEntity<TopicResponseDto> getTodayTopic() {
        return ResponseEntity.ok(topicService.getTodayTopic());
    }
}
