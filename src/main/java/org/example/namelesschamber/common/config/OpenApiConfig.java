package org.example.namelesschamber.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "NamelessChamber API",
                version = "v1",
                description = "무명소 백엔드 공개 API 문서",
                contact = @Contact(email = "respectwo@naver.com")
        )
)
public class OpenApiConfig { }
