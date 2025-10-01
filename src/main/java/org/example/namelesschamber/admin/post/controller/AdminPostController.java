package org.example.namelesschamber.admin.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.admin.post.dto.request.AdminPostRequestDto;
import org.example.namelesschamber.admin.post.dto.response.AdminPostResponseDto;
import org.example.namelesschamber.admin.post.service.AdminPostService;
import org.example.namelesschamber.domain.post.entity.PostType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Post API", description = "관리자 전용 게시글 관리 API")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPostController {

    private final AdminPostService adminPostService;

    @Operation(summary = "게시글 목록 조회", description = "관리자가 게시글을 조회합니다. type 파라미터로 전체/짧은글/긴글을 구분할 수 있습니다.")
    @GetMapping("/posts")
    public ResponseEntity<List<AdminPostResponseDto>> getPosts(
            @RequestParam(required = false) PostType type
    ) {
        return ResponseEntity.ok(adminPostService.getPosts(type));
    }
    @Operation(summary = "게시글 상세 조회", description = "관리자가 특정 게시글을 조회합니다.")
    @GetMapping("/posts/{id}")
    public ResponseEntity<AdminPostResponseDto> getPost(@PathVariable String id) {
        return ResponseEntity.ok(adminPostService.getPostById(id));
    }

    @Operation(summary = "게시글 수정", description = "관리자가 특정 게시글을 수정합니다.")
    @PatchMapping("/posts/{id}")
    public ResponseEntity<Void> updatePost(
            @PathVariable String id,
            @Valid
            @RequestBody AdminPostRequestDto request
    ) {
        adminPostService.updatePost(id, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시글 삭제", description = "관리자가 특정 게시글을 삭제합니다. (Soft Delete)")
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        adminPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}