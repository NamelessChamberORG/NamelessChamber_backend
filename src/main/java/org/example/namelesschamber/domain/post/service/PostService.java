package org.example.namelesschamber.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.example.namelesschamber.common.exception.CustomException;
import org.example.namelesschamber.domain.post.dto.response.PostResponseDto;
import org.example.namelesschamber.domain.post.entity.Post;
import org.example.namelesschamber.domain.post.repository.PostRepository;
import org.example.namelesschamber.domain.post.repository.RandomPostFinder;
import org.example.namelesschamber.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final RandomPostFinder randomPostFinder;

    public PostResponseDto getPost(User user) {

        Post post = randomPostFinder.find()
                .orElseThrow(() -> new CustomException("게시글이 없습니다."));

        return PostResponseDto.from(post);
    }
}
