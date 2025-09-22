package org.example.namelesschamber.domain.post.repository;

import org.example.namelesschamber.domain.post.entity.Post;
import org.example.namelesschamber.domain.post.entity.PostType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByTypeOrderByCreatedAtDesc(PostType type);
    List<Post> findAllByUserIdOrderByCreatedAtDesc(String userId);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
