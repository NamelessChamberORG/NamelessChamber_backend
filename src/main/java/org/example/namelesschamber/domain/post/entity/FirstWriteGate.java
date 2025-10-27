package org.example.namelesschamber.domain.post.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "first_write_gate")
@CompoundIndex(name = "uniq_user_day", def = "{'userId': 1, 'dayStart': 1}", unique = true)
@Data
public class FirstWriteGate {
    @Id
    private String id;

    @Indexed
    private String userId;
    @Indexed
    private Instant dayStart;

    @Indexed(name = "ttl_cleanup_2d", expireAfter = "P2D")
    private Instant createdAt;}