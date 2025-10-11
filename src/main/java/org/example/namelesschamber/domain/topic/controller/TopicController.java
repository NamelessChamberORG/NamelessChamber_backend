package org.example.namelesschamber.domain.topic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Topics API", description = "주제 API")

public class TopicController {

    private final TopicService topicService;

    @Operation(
            summary = "오늘의 주제 조회",
            description = "현재 발행된 오늘의 주제를 조회합니다."
    )
    @GetMapping("/today")
    public ResponseEntity<TopicResponseDto> getTodayTopic() {
        return ResponseEntity.ok(topicService.getTodayTopic());
    }
}
