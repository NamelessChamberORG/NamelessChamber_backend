package org.example.namelesschamber.domain.readhistory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.response.ApiResponse;
import org.example.namelesschamber.domain.auth.core.SecurityUtils;
import org.example.namelesschamber.domain.post.dto.response.PostPreviewListResponse;
import org.example.namelesschamber.domain.readhistory.service.ReadHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/read-history")
@Tag(name = "ReadHistory", description = "열람 이력 API")
public class ReadHistoryController {

    private final ReadHistoryService readHistoryService;

    @Operation(
            summary = "내가 열람한 일기 목록 조회",
            description = "사용자가 열람한 일기를 최신순으로 반환"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<PostPreviewListResponse>> getMyReadPosts() {
        String userId = SecurityUtils.getCurrentSubject();
        PostPreviewListResponse response = readHistoryService.getMyReadPosts(userId);
        return ApiResponse.success(HttpStatus.OK,response);
    }
}
