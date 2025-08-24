package org.example.namelesschamber.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.exception.PostNotFoundException;
import org.example.namelesschamber.domain.post.dto.request.PostCreateRequestDto;
import org.example.namelesschamber.domain.post.dto.response.PostDetailResponseDto;
import org.example.namelesschamber.domain.post.dto.response.PostPreviewResponseDto;
import org.example.namelesschamber.domain.post.entity.Post;
import org.example.namelesschamber.domain.post.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<PostPreviewResponseDto> getPostPreviews() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostPreviewResponseDto::from)
                .toList();
    }
    public void createPost(PostCreateRequestDto request, String anonymousToken) {

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .type(request.type())
                .anonymousToken(anonymousToken)
                .build();

        postRepository.save(post);
    }

    public PostDetailResponseDto getPostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        post.increaseViews();
        postRepository.save(post);

        return PostDetailResponseDto.from(post);
    }

}