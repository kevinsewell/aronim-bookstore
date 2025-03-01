# Aronim Bookstore

A modern, cloud-ready bookstore application built with Spring Boot and following best practices for enterprise application development.

## Project Overview

Aronim Bookstore is a comprehensive bookstore management system that allows for inventory management, customer management, order processing, and online purchasing of books. The application is built using a modular architecture with a focus on maintainability, scalability, and observability.

## Technology Stack

### Core Framework and Language
- **Spring Boot** - Primary application framework
- **Java** - Main programming language
- **Gradle (Kotlin DSL)** - Build tool

### Architecture
- **Modulith** - Modular monolith architecture style
- **Layered Architecture** - Controller, Service, Repository layers

### Data Layer
- **PostgreSQL** - Primary database
- **Spring Data JPA** - Data access layer
- **Liquibase/Flyway** - Database schema migration options
- **Hibernate** - ORM framework

### API & Web
- **REST APIs** - Following standardized design principles
- **API Versioning** - URI path-based versioning strategy
- **JSON** - Primary data exchange format
- **OpenAPI/Swagger** - API documentation

### Security
- **Spring Security** - Security framework
- **Keycloak** - Identity and access management (both containerized and embedded options)
- **OAuth 2.0/JWT** - Authentication mechanism
- **RBAC** - Role-Based Access Control

### Caching
- **Spring Cache Abstraction** - Caching framework
- **Caffeine** - In-memory cache provider
- **Redis** - Distributed cache (optional)

### Monitoring & Observability
- **Micrometer** - Metrics collection
- **Prometheus** - Metrics storage
- **Grafana** - Metrics visualization
- **ELK Stack** - Log aggregation and analysis
- **OpenTelemetry** - Distributed tracing
- **Spring Boot Actuator** - Application monitoring

### Testing
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework
- **Testcontainers** - Integration testing with real dependencies
- **Spring Boot Test** - Spring application testing

## Project Structure

```
aronim-bookstore/
├── buildSrc/                   # Shared build logic
├── gradle/                     # Gradle wrapper and configuration
├── app/                        # Main application module
│   ├── src/main/java/          # Java source files
│   ├── src/main/resources/     # Configuration files
│   └── src/test/               # Tests
├── domain/                     # Domain model module
├── infrastructure/             # Infrastructure components
├── api/                        # API definitions and controllers
├── docs/                       # Documentation
│   └── adr/                    # Architecture Decision Records
└── docker/                     # Docker configurations
```

## Getting Started

### Prerequisites
- JDK 17 or higher
- Docker and Docker Compose
- Gradle 7.4+ (or use the included Gradle wrapper)

### Building the Project
```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Build without tests
./gradlew build -x test
```

### Running Locally
```bash
# Start required infrastructure (PostgreSQL, etc.)
docker-compose -f docker/local/docker-compose.yml up -d

# Run the application
./gradlew bootRun

# Run with a specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### API Documentation
Once the application is running, you can access the API documentation at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Development Guidelines

### Code Style
- Follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use the provided `.editorconfig` for consistent formatting
- Run `./gradlew checkstyleMain` to verify code style compliance

### Branching Strategy
- `main` - Main branch, always stable
- `develop` - Development branch
- Feature branches: `feature/feature-name`
- Bugfix branches: `bugfix/bug-description`
- Release branches: `release/version`

### Commit Messages
Follow the [Conventional Commits](https://www.conventionalcommits.org/) specification:
```
feat: add new customer registration feature
fix: resolve issue with order calculation
docs: update API documentation
refactor: simplify order processing logic
```

### Database Changes
- All database changes must be made through Liquibase or Flyway migrations (based on the selected tool)
- Migration files should be placed in the appropriate resources directory
- Follow the naming conventions specified in the ADRs

### Testing
- Write unit tests for all business logic
- Use integration tests for repository and API layers
- Aim for high test coverage on domain logic
- Use Testcontainers for integration testing with real databases

## Architecture

### Modulith Architecture
The application follows a modulith architecture style, which combines the benefits of a monolith (simplicity, development speed) with those of microservices (modularity, clear boundaries).

Key characteristics:
- Single deployable application with multiple well-defined business modules
- Explicit module boundaries enforced through package structures
- Internal event-based communication between modules
- Module-specific persistence
- Shared kernel for truly common components

### REST API Design
The application exposes RESTful APIs following standardized design principles:
- Resource-oriented URLs
- Appropriate HTTP methods (GET, POST, PUT, PATCH, DELETE)
- Consistent error responses
- Versioning through URI path (e.g., `/api/v1/books`)
- Comprehensive documentation with OpenAPI/Swagger

### Security
The application uses a multi-layered security approach:
- OAuth 2.0 with JWT for authentication
- Role-Based Access Control (RBAC) for authorization
- Keycloak for identity management (with options for both containerized and embedded deployment)
- HTTPS for all communications
- Proper input validation and output encoding

### Monitoring and Observability
The application includes comprehensive monitoring and observability features:
- Health checks and metrics via Spring Boot Actuator
- Detailed logging with correlation IDs
- Distributed tracing for request flows
- Business metrics for key operations
- Performance monitoring and alerting

## Configuration

### Application Properties
The application uses a hierarchical configuration approach:
- `application.yml` - Default configuration
- `application-{profile}.yml` - Environment-specific configuration
- Environment variables - Runtime configuration

Key configuration properties:
```yaml
spring:
  application:
    name: aronim-bookstore
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:bookstore}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
  jpa:
    hibernate:
      ddl-auto: validate
  # Choose either Liquibase or Flyway based on ADR decision
  liquibase:
    enabled: ${LIQUIBASE_ENABLED:false}
    change-log: classpath:db/changelog/db.changelog-master.xml
  flyway:
    enabled: ${FLYWAY_ENABLED:true}
    baseline-on-migrate: true
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${JWT_ISSUER_URI:http://localhost:8080/auth/realms/bookstore}
```

## Documentation

### Architecture Decision Records (ADRs)
The project maintains Architecture Decision Records to document significant architectural decisions:

1. [ADR-001: Adoption of Gradle as Build Tools](docs/adr/001_adoption_of_gradle_as_build_tools.md)
2. [ADR-002: Adoption of Java](docs/adr/002_adoption_of_java.md)
3. [ADR-003: Kotlin DSL for Gradle Build Configuration](docs/adr/003_kotlin_dsl_for_gradle_build_configuration.md)
4. [ADR-004: Adoption of Spring Boot](docs/adr/004_adoption_of_spring_boot.md)
5. [ADR-005: Modulith Architecture](docs/adr/005_modulith_architecture.md)
6. [ADR-006: Layered Architecture](docs/adr/006_layered_architecture.md)
7. [ADR-007: REST API Design Standards](docs/adr/007_rest_api_design_standards.md)
8. [ADR-008: API Versioning Strategy](docs/adr/008_api_versioning_strategy.md)
9. [ADR-009: API Documentation Strategy](docs/adr/009_api_documentation_strategy.md)
10. [ADR-010: API Security Strategy](docs/adr/010_api_security_strategy.md)
11. [ADR-011: Adoption of Keycloak (Containerized)](docs/adr/011_adoption_of_keycloak_containerized.md)
12. [ADR-012: Adoption of Keycloak (Embedded)](docs/adr/012_adoption_of_keycloak_embedded.md)
13. [ADR-013: Adoption of PostgreSQL](docs/adr/013_adoption_of_postgresql.md)
14. [ADR-014: Adoption of Spring Data JPA](docs/adr/014_adoption_of_spring_data_jpa.md)
15. [ADR-015: Adoption of Liquibase](docs/adr/015_adoption_of_liquibase.md)
16. [ADR-016: Adoption of Flyway](docs/adr/016_adoption_of_flyway.md)
17. [ADR-017: Caching Strategy](docs/adr/017_caching_strategy.md)
18. [ADR-018: Centralized Exception Handling](docs/adr/018_centralized_exception_handling.md)
19. [ADR-019: Logging Framework and Standards](docs/adr/019_logging_framework_and_standards.md)
20. [ADR-020: Monitoring and Observation Strategy](docs/adr/020_monitoring_and_observation_strategy.md)

## Deployment

### Docker
The application can be containerized using the provided Dockerfile:
```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY app/build/libs/application.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Kubernetes
Kubernetes deployment manifests are available in the `kubernetes/` directory:
- Deployment configuration
- Service definition
- ConfigMap for environment-specific settings
- Secret management
- Health check configuration

## Contributing
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Contact
Project Maintainer: [Your Name](mailto:your.email@example.com)

## Acknowledgements
- Spring Boot and the Spring team
- The Java community
- All open-source libraries used in this project
