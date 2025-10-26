package org.example.namelesschamber.domain.user.repository;

import org.example.namelesschamber.domain.user.entity.User;
import org.example.namelesschamber.domain.user.entity.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAllByUserRole(UserRole role);
    Optional<User> findByNicknameAndUserRole(String nickname, UserRole role);
    Optional<User> findByIdAndUserRole(String id, UserRole role);
    boolean existsByNickname(String nickname);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByUserRoleAndCreatedAtBetween(UserRole role, Instant start, Instant end);
    long countByUserRole(UserRole role);
}
