package org.example.namelesschamber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class NamelessChamberApplication  {
    public static void main(String[] args) {
        SpringApplication.run(NamelessChamberApplication.class, args);
    }
}