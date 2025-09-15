package org.example.namelesschamber.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SwaggerSecurityConfig {

    private static final String SWAGGER_ROLE = "ADMIN";

    @Bean
    public UserDetailsService swaggerUserDetailsService(
            @Value("${swagger.username}") String username,
            @Value("${swagger.password}") String password,
            PasswordEncoder encoder
    ) {
        UserDetails swaggerUser = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .roles(SWAGGER_ROLE)
                .build();

        return new InMemoryUserDetailsManager(swaggerUser);
    }

}
