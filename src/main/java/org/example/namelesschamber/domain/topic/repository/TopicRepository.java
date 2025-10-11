package org.example.namelesschamber.domain.topic.repository;

import org.example.namelesschamber.domain.topic.entity.Topic;
import org.example.namelesschamber.domain.topic.entity.TopicStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TopicRepository extends MongoRepository<Topic, String> {

    Optional<Topic> findByStatusAndPublishedDate(TopicStatus status, LocalDate publishedDate);
    List<Topic> findAllByStatusAndPublishedDate(TopicStatus status, LocalDate publishedDate);
    Optional<Topic> findFirstByStatusOrderByIdAsc(TopicStatus status);
    Optional<Topic> findFirstByStatusAndIdGreaterThanOrderByIdAsc(TopicStatus status, String id);
    List<Topic> findAllByStatusAndPublishedDateBefore(TopicStatus status, LocalDate publishedDate);
    Optional<Topic> findTopByStatusOrderByIdDesc(TopicStatus status);

}
