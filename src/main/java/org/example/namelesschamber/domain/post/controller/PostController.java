package org.example.namelesschamber.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.response.ApiResponse;
import org.example.namelesschamber.domain.auth.core.SecurityUtils;
import org.example.namelesschamber.domain.post.dto.request.PostCreateRequestDto;
import org.example.namelesschamber.domain.post.dto.response.PostCreateResponseDto;
import org.example.namelesschamber.domain.post.dto.response.PostDetailResponseDto;
import org.example.namelesschamber.domain.post.dto.response.PostPreviewListResponse;
import org.example.namelesschamber.domain.post.dto.response.PostPreviewResponseDto;
import org.example.namelesschamber.domain.post.entity.PostType;
import org.example.namelesschamber.domain.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "Posts", description = "게시글 API")
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "글 조회",
            description = "게시물의 미리보기 리스트를 반환합니다. 'type' 쿼리 파라미터로 특정 타입의 글만 조회할 수 있습니다."
    )
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<PostPreviewListResponse>> getPosts(
            @RequestParam(required = false) PostType type) {

        String userId = SecurityUtils.getCurrentSubject();

        PostPreviewListResponse response =
                (type == null)
                        ? postService.getPostPreviews(userId)
                        : postService.getPostPreviews(type, userId);

        return ApiResponse.success(HttpStatus.OK, response);
    }
    @Operation(
            summary = "글 작성",
            description = "새로운 게시글을 작성합니다"
    )
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostCreateResponseDto>> createPost(
            @RequestBody @Valid PostCreateRequestDto request) {

        String subject = SecurityUtils.getCurrentSubject();

        PostCreateResponseDto response = postService.createPost(request, subject);
        return ApiResponse.success(HttpStatus.CREATED, response);
    }

    @Operation(summary = "특정 글 조회", description = "게시글 ID로 특정 게시글의 상세 내용을 조회합니다.")
    @GetMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<PostDetailResponseDto>> getPostById(@PathVariable String id) {

        String subject = SecurityUtils.getCurrentSubject();

        PostDetailResponseDto response = postService.getPostById(id, subject);
        return ApiResponse.success(HttpStatus.OK, response);
    }

    @Operation(summary = "내가 쓴 글 조회", description = "내가 작성한 게시글 목록을 반환합니다.")
    @GetMapping("/posts/me")
    public ResponseEntity<ApiResponse<List<PostPreviewResponseDto>>> getMyPosts() {

        String subject = SecurityUtils.getCurrentSubject();

        List<PostPreviewResponseDto> response = postService.getMyPosts(subject);
        return ApiResponse.success(HttpStatus.OK,response);
    }

}

