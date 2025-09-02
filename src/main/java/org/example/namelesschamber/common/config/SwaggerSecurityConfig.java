package org.example.namelesschamber.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
@Profile("prod")
public class SwaggerSecurityConfig {

    @Bean
    public UserDetailsService swaggerUsers(
            @Value("${swagger.username}") String username,
            @Value("${swagger.password}") String password,
            PasswordEncoder encoder
    ) {
        UserDetails swaggerUser = User.builder()
                .username(username)
                .password(encoder.encode(password))
                .roles("DEV")
                .build();

        return new InMemoryUserDetailsManager(swaggerUser);
    }

    @Bean
    public SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger/**").hasRole("DEV")
                        .anyRequest().permitAll()
                )
                .formLogin(Customizer.withDefaults());
                return http.build();
    }
}
