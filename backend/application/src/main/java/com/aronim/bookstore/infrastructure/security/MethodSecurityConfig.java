package com.aronim.bookstore.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // Remove the ROLE_ prefix so we can use @PreAuthorize("hasRole('ADMIN')") instead of @PreAuthorize("hasRole('ROLE_ADMIN')")
        return new GrantedAuthorityDefaults("");
    }
}
