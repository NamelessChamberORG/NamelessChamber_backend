package org.example.namelesschamber.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class MongoIndexConfig {

    private final MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        mongoTemplate.indexOps("users")
                .ensureIndex(new Index()
                        .on("expiresAt", Sort.Direction.ASC)
                        .expire(0, TimeUnit.SECONDS));
    }
}
