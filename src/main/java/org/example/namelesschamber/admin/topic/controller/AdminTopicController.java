package org.example.namelesschamber.admin.topic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.admin.topic.service.AdminTopicService;
import org.example.namelesschamber.domain.topic.dto.request.TopicRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Topic API", description = "관리자 주제 관리 API")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTopicController {

    private final AdminTopicService adminTopicService;
    @Operation(summary = "새로운 주제 등록", description = "관리자가 새로운 오늘의 주제를 등록합니다.")
    @PostMapping("/topics")
    public ResponseEntity<Void> createTopic(@RequestBody TopicRequestDto requestDto) {
        adminTopicService.createTopic(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}