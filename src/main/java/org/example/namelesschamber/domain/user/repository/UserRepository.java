package org.example.namelesschamber.domain.user.repository;

import org.example.namelesschamber.domain.user.entity.User;
import org.example.namelesschamber.domain.user.entity.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByNickname(String nickname);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByUserRoleAndCreatedAtBetween(UserRole role, LocalDateTime start, LocalDateTime end);
}
