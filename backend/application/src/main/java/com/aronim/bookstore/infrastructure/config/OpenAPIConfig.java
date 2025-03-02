package com.aronim.bookstore.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${aronim.openapi.dev-url}")
    private String devUrl;

    @Value("${aronim.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server()
                .url(devUrl)
                .description("Server URL in Development environment");

        Server prodServer = new Server()
                .url(prodUrl)
                .description("Server URL in Production environment");

        Contact contact = new Contact()
                .name("Aronim Bookstore")
                .email("support@aronim.com")
                .url("https://aronim.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Aronim Bookstore API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage the Aronim Bookstore.")
                .termsOfService("https://aronim.com/terms")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }
}
