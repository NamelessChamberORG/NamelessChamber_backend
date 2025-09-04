package org.example.namelesschamber.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

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
