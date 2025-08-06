package org.example.namelesschamber.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.response.ApiResponse;
import org.example.namelesschamber.domain.post.dto.response.PostResponseDto;
import org.example.namelesschamber.domain.post.service.PostService;
import org.example.namelesschamber.domain.user.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ApiResponse<PostResponseDto> getPost(User user){

        PostResponseDto response = postService.getPost(user);

        return ApiResponse.success(response);
    }
}
