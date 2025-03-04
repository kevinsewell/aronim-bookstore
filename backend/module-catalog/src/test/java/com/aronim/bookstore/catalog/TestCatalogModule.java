package com.aronim.bookstore.catalog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
@SpringBootApplication(scanBasePackages = {
        "com.aronim.bookstore.catalog",
        "com.aronim.bookstore.security.test"
})
public class TestCatalogModule {

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // Remove the ROLE_ prefix so we can use @PreAuthorize("hasRole('ADMIN')") instead of @PreAuthorize("hasRole('ROLE_ADMIN')")
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
