package com.aronim.bookstore.security.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import static org.mockito.Mockito.mock;

@Configuration
@Profile("test")
public class TestSecurityConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        return mock(NimbusJwtDecoder.class);
    }
}
