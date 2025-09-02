package org.example.namelesschamber.domain.feedback.repository;

import org.example.namelesschamber.domain.feedback.entity.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackRepository extends MongoRepository<Feedback, String> {
}
