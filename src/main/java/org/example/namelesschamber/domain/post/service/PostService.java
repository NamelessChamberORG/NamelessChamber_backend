package org.example.namelesschamber.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.domain.post.dto.request.PostCreateRequestDto;
import org.example.namelesschamber.domain.post.dto.response.PostDetailResponseDto;
import org.example.namelesschamber.domain.post.dto.response.PostPreviewResponseDto;
import org.example.namelesschamber.domain.post.entity.Post;
import org.example.namelesschamber.domain.post.entity.PostType;
import org.example.namelesschamber.domain.post.repository.PostRepository;
import org.example.namelesschamber.domain.user.entity.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<PostPreviewResponseDto> getPostPreviews() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostPreviewResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PostPreviewResponseDto> getPostPreviews(PostType type) {
        return postRepository.findAllByTypeOrderByCreatedAtDesc(type).stream()
                .map(PostPreviewResponseDto::from)
                .toList();
    }

    @Transactional
    public void createPost(PostCreateRequestDto request, String subject, String role) {
        request.type().validateContentLength(request.content());

        Post.PostBuilder builder = Post.builder()
                .title(request.title())
                .content(request.content())
                .type(request.type());

        if (UserRole.USER.name().equals(role)) {
            builder.userId(subject);          // 회원이면 userId 저장
        } else if (UserRole.ANONYMOUS.name().equals(role)) {
            builder.anonymousToken(subject);  // 익명이면 uuid 저장
        } else {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        postRepository.save(builder.build());
    }


    @Transactional
    public PostDetailResponseDto getPostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.increaseViews();

        return PostDetailResponseDto.from(post);
    }

}