package org.example.namelesschamber.domain.readhistory.repository;

import org.example.namelesschamber.domain.readhistory.entity.ReadHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReadHistoryRepository extends MongoRepository<ReadHistory, String> {

    boolean existsByUserIdAndPostId(String userId, String postId);

    Optional<ReadHistory> findByUserIdAndPostId(String userId, String postId);

    List<ReadHistory> findAllByUserIdOrderByReadAtDesc(String userId);
}
