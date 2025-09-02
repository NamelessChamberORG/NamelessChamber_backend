package org.example.namelesschamber.domain.feedback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.response.ApiResponse;
import org.example.namelesschamber.domain.feedback.dto.request.FeedbackRequestDto;
import org.example.namelesschamber.domain.feedback.dto.response.FeedbackResponseDto;
import org.example.namelesschamber.domain.feedback.service.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedbacks")
@Tag(name = "Feedbacks", description = "피드백 API")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "피드백 작성", description = "새로운 피드백을 작성합니다. 회원/익명 모두 가능.")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createFeedback(
            @RequestBody @Valid FeedbackRequestDto request,
            @CookieValue(value = "anonymousToken", required = false) String anonymousToken) {

        feedbackService.createFeedback(request, anonymousToken);
        return ApiResponse.success(HttpStatus.CREATED);
    }

    @Operation(summary = "피드백 조회", description = "저장된 피드백 리스트를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<FeedbackResponseDto>>> getAllFeedbacks() {
        List<FeedbackResponseDto> response = feedbackService.getAllFeedbacks();
        return ApiResponse.success(HttpStatus.OK, response);
    }
}
