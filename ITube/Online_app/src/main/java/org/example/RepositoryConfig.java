package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class RepositoryConfig {

    @Bean
    public VideoRepositoryImpl videoRepository(JdbcTemplate jdbcTemplate) {
        return new VideoRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public UserRepositoryImpl userRepository(JdbcTemplate jdbcTemplate) {
        return new UserRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public CommentRepositoryImpl commentRepository (JdbcTemplate jdbcTemplate) {
        return new CommentRepositoryImpl(jdbcTemplate);
    }
}
