package com.aronim.bookstore.catalog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;

@EnableWebSecurity
@EnableMethodSecurity
@ActiveProfiles("test")
@SpringBootApplication(scanBasePackages = {
        "com.aronim.bookstore.catalog",
        "com.aronim.bookstore.security",
        "com.aronim.bookstore.security.test"
})
public class TestCatalogModule {
}
