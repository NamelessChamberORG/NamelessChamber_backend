package org.example.namelesschamber.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.response.ApiResponse;
import org.example.namelesschamber.domain.post.dto.request.PostCreateRequestDto;
import org.example.namelesschamber.domain.post.dto.response.PostResponseDto;
import org.example.namelesschamber.domain.post.service.PostService;
import org.example.namelesschamber.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "글 조회", description = "저장된 게시글 중 무작위로 하나를 조회합니다.")
    @GetMapping("/posts")
    public ApiResponse<PostResponseDto> getPost(){

        PostResponseDto response = postService.getPost();

        return ApiResponse.success(response);
    }
    @Operation(summary = "글 작성", description = "익명 사용자가 새로운 게시글을 작성합니다.")
    @PostMapping("/posts")
    public void createPost(@RequestBody @Valid PostCreateRequestDto request,
                           @CookieValue("anonymousToken") String anonymousToken) {
        postService.createPost(request, anonymousToken);
    }


}
