package org.example.namelesschamber.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.common.exception.ErrorCode;
import org.example.namelesschamber.domain.post.dto.request.PostCreateRequestDto;
import org.example.namelesschamber.domain.post.dto.response.PostDetailResponseDto;
import org.example.namelesschamber.domain.post.dto.response.PostPreviewResponseDto;
import org.example.namelesschamber.domain.post.entity.Post;
import org.example.namelesschamber.domain.post.repository.PostRepository;
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

    @Transactional
    public void createPost(PostCreateRequestDto request, String anonymousToken) {
        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .type(request.type())
                .anonymousToken(anonymousToken)
                .build();

        postRepository.save(post);
    }

    @Transactional
    public PostDetailResponseDto getPostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.increaseViews();

        return PostDetailResponseDto.from(post);
    }

}