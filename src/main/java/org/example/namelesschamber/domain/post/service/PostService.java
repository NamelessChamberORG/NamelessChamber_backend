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
import org.example.namelesschamber.domain.user.entity.User;
import org.example.namelesschamber.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

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
    public void createPost(PostCreateRequestDto request, String subject) {
        request.type().validateContentLength(request.content());

        User user = userRepository.findById(subject)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.addCoin(1);
        userRepository.save(user);

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .type(request.type())
                .userId(subject)
                .build();

        postRepository.save(post);
    }

    @Transactional
    public PostDetailResponseDto getPostById(String postId, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getCoin() <= 0) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_COIN);
        }

        user.decreaseCoin();
        userRepository.save(user);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.increaseViews();
        postRepository.save(post);

        return PostDetailResponseDto.from(post);
    }

}