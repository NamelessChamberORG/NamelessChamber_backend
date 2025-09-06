package org.example.namelesschamber.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.response.ApiResponse;
import org.example.namelesschamber.common.security.SecurityUtils;
import org.example.namelesschamber.domain.post.dto.request.PostCreateRequestDto;
import org.example.namelesschamber.domain.post.dto.response.PostDetailResponseDto;
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

//    @Operation(summary = "글 조회", description = "저장된 게시글 중 무작위로 하나를 조회합니다.")
//    @GetMapping("/posts")
//    public ApiResponse<PostResponseDto> getPost(){
//
//        PostResponseDto response = postService.getPost();
//
//        return ApiResponse.success(response);
//    }

    @Operation(summary = "글 조회", description = "게시물의 미리보기 리스트를 반환합니다. 'type' 쿼리 파라미터로 특정 타입의 글만 조회할 수 있습니다.")
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<PostPreviewResponseDto>>> getPosts(
            @RequestParam(required = false) PostType type) {

        List<PostPreviewResponseDto> response =
                (type == null) ? postService.getPostPreviews() : postService.getPostPreviews(type);

        return ApiResponse.success(HttpStatus.OK, response);
    }

    @Operation(
            summary = "글 작성",
            description = "새로운 게시글 작성"
    )
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<Void>> createPost(
            @RequestBody @Valid PostCreateRequestDto request) {

        String subject = SecurityUtils.getCurrentSubject();

        postService.createPost(request, subject);
        return ApiResponse.success(HttpStatus.CREATED);
    }

    @Operation(summary = "특정 글 조회", description = "게시글 ID로 특정 게시글의 상세 내용을 조회합니다.")
    @GetMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<PostDetailResponseDto>> getPostById(@PathVariable String id) {

        String subject = SecurityUtils.getCurrentSubject();

        PostDetailResponseDto response = postService.getPostById(id, subject);
        return ApiResponse.success(HttpStatus.OK, response);
    }
}

