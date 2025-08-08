package org.example.namelesschamber.domain.post.repository;

import org.example.namelesschamber.domain.post.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
