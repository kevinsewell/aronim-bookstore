# Summary of Architectural Decisions

## Build and Development Environment

1. **Gradle as Build Tool** (ADR-001)
   - Selected Gradle for build automation due to its flexibility, performance, and plugin ecosystem
   - Provides incremental builds, build cache, and comprehensive dependency management
   - Better support for multi-module projects compared to alternatives

2. **Java as Programming Language** (ADR-002)
   - Adopted Java (JDK 21) as the primary programming language
   - Leverages modern Java features like virtual threads, pattern matching, and records
   - Provides strong type safety, mature ecosystem, and excellent Spring Boot integration

3. **Kotlin DSL for Gradle Configuration** (ADR-003)
   - Implemented Gradle build scripts using Kotlin DSL instead of Groovy
   - Provides better IDE support, type safety, and refactoring capabilities
   - Improves build script maintainability and discoverability

4. **Spring Boot as Application Framework** (ADR-004)
   - Selected Spring Boot as the core application framework
   - Offers production-ready defaults, comprehensive ecosystem, and excellent integration options
   - Provides robust support for enterprise application development patterns

## Architecture and Structure

5. **Modulith Architecture** (ADR-005)
   - Adopted a modular monolith ("Modulith") architecture style
   - Combines benefits of monolith (simplicity, development speed) with microservices benefits (modularity)
   - Organizes code into well-defined business domains with explicit boundaries
   - Enables potential future extraction of microservices if needed

6. **Layered Architecture** (ADR-006)
   - Implemented a layered architecture within each module
   - Consists of controller, service, repository, and domain layers
   - Provides clear separation of concerns and better testability
   - Complements the Modulith approach by defining internal module structure

## API Design and Documentation

7. **REST API Design Standards** (ADR-007)
   - Established consistent standards for RESTful API design
   - Defined resource naming conventions, HTTP method usage, and response formats
   - Implemented standardized error handling and status code usage

8. **API Versioning Strategy** (ADR-008)
   - Adopted URI path versioning (e.g., `/api/v1/resource`)
   - Defined policy for when to create new versions
   - Established approach for maintaining backward compatibility

9. **API Documentation Strategy** (ADR-009)
   - Selected OpenAPI/Swagger for API documentation
   - Implemented code-first documentation approach using annotations
   - Provides interactive API documentation for developers

10. **API Security Strategy** (ADR-010)
    - Implemented OAuth 2.0 with JWT for authentication and authorization
    - Defined Role-Based Access Control (RBAC) model
    - Established security headers, HTTPS requirements, and input validation approach

## Identity and Access Management

11. **Keycloak for Identity Management** (ADR-011, ADR-012)
    - Selected Keycloak as the identity and access management solution
    - **Adopted** containerized deployment approach (ADR-011)
    - **Rejected** embedded deployment within the application (ADR-012)
    - Provides comprehensive identity management, OAuth 2.0/OpenID Connect support

## Data Management

13. **PostgreSQL as Primary Database** (ADR-013)
    - Selected PostgreSQL as the primary relational database
    - Leverages advanced features like JSON support, full-text search, and robust transaction handling
    - Provides excellent performance, reliability, and feature set

14. **Spring Data JPA for Data Access** (ADR-014)
    - Implemented Spring Data JPA for data access layer
    - Reduces boilerplate code through repositories
    - Provides consistent approach to data access across the application

15. **Database Schema Management** (ADR-015, ADR-016)
    - **Rejected** Liquibase for database migrations (ADR-015)
    - **Adopted** Flyway for database schema evolution (ADR-016)
    - Selected SQL-first approach for better clarity and control
    - Implements versioned migrations for tracking schema changes

## Performance and Operations

17. **Caching Strategy** (ADR-017)
    - Implemented multi-level caching with Spring Cache Abstraction
    - Uses Caffeine for local in-memory caching
    - Optional Redis integration for distributed caching
    - Defined cache regions, policies, and invalidation strategies

18. **Centralized Exception Handling** (ADR-018)
    - Implemented global exception handling with @ControllerAdvice
    - Created consistent error response format
    - Defined exception hierarchy and mapping to HTTP status codes
    - Provides better user experience and easier debugging

19. **Logging Framework and Standards** (ADR-019)
    - Selected SLF4J with Logback for logging
    - Implemented structured logging with JSON format
    - Established logging levels and usage guidelines
    - Configured centralized log aggregation

20. **Monitoring and Observability Strategy** (ADR-020)
    - Implemented the three pillars of observability: metrics, logs, and traces
    - Selected Micrometer, Prometheus, and Grafana for metrics
    - Integrated OpenTelemetry for distributed tracing
    - Configured health checks and alerting

## Testing and Quality Assurance

21. **Testing Pyramid Implementation** (ADR-021)
    - Established a comprehensive testing strategy based on the testing pyramid
    - Implemented unit, integration, API, and end-to-end tests
    - Selected JUnit 5, Mockito, TestContainers, and Cucumber as testing tools
    - Defined CI/CD integration for test execution

22. **Test Data Management** (ADR-022)
    - Implemented test data builders pattern for consistent test data
    - Created layer-specific test data strategies
    - Established test fixtures and factories for common scenarios
    - Configured test data isolation and cleanup

## Deployment and Operations

23. **Containerization Strategy** (ADR-023)
    - Selected Docker for application containerization
    - Implemented multi-stage builds for optimized images
    - Established security practices for container hardening
    - Created development and production container configurations

24. **CI/CD Pipeline Design** (ADR-024)
    - Implemented comprehensive CI/CD pipeline using GitHub Actions
    - Defined pipeline stages for build, test, analysis, and deployment
    - Established environment promotion strategy
    - Configured quality gates and security checks

## Frontend Architecture

25. **Micro-Frontend Architecture** (ADR-025)
    - Adopted a micro-frontend architecture for web applications
    - Implemented runtime integration approach using module federation
    - Created vertically sliced micro-frontends aligned with backend domains
    - Established patterns for shared components, state management, and communication
    - Configured independent deployment and CI/CD for each micro-frontend
    - Implemented a thin application shell for orchestration and shared functionality

26. **Backend-for-Frontend (BFF) Pattern** (ADR-026)
    - Implemented dedicated BFF services tailored to specific frontend clients
    - Adopted GraphQL for flexible data fetching and aggregation
    - Created BFFs aligned with micro-frontend domain boundaries
    - Implemented caching, security, and resilience patterns at the BFF layer
    - Established clear contracts between BFFs and frontend applications
    - Configured independent deployment and scaling for BFF services

27. **React as Default Frontend Framework** (ADR-027)
    - Selected React as the primary frontend framework for web applications
    - Implemented TypeScript for type safety and developer experience
    - Created component-based architecture following atomic design principles
    - Established shared component library and design system
    - Configured React for module federation and micro-frontend integration
    - Implemented performance optimizations like code splitting and lazy loading
    - Selected React Query for data fetching and state management

28. **React Native for Mobile Development** (ADR-028)
    - Adopted React Native for cross-platform mobile application development
    - Implemented single codebase targeting both iOS and Android
    - Aligned mobile architecture with micro-frontend and BFF approaches
    - Established shared component libraries between web and mobile where possible
    - Configured CI/CD pipelines for automated building and testing
    - Implemented responsive designs that adapt to various device sizes
    - Selected appropriate navigation, state management, and UI libraries

## Compliance and Globalization

29. **GDPR Compliance Strategy** (ADR-029)
    - Implemented comprehensive GDPR compliance framework
    - Created centralized Privacy Module within Modulith architecture
    - Developed user consent management system with granular options
    - Implemented automated tools for handling data subject requests
    - Established data mapping, classification, and retention policies
    - Created audit logging for all personal data access and modifications
    - Implemented pseudonymization and encryption for personal data
    - Designed user-friendly interfaces for privacy settings and data requests
    - Appointed Data Protection Officer and privacy governance committee
    - Established data breach notification procedures and impact assessments

30. **Internationalization and Localization Strategy** (ADR-030)
    - Implemented comprehensive i18n and l10n approach across all platforms
    - Used Spring's MessageSource for backend internationalization
    - Adopted React-i18next for web frontend and React Native i18n
    - Implemented locale detection based on user preferences and system settings
    - Created shared translation management system for all platforms
    - Supported lazy loading of translation files by language
    - Developed RTL layout support using CSS logical properties
    - Established consistent formatting for dates, numbers, and currencies
    - Created workflow for translation updates and quality assurance
    - Implemented fallback mechanisms for missing translations

## Key Technology Decisions Summary

- **Build System**: Gradle with Kotlin DSL
- **Language**: Java 21
- **Framework**: Spring Boot
- **Architecture**: Modulith with layered internal structure
- **API Design**: RESTful with OpenAPI documentation
- **Security**: OAuth 2.0/JWT with Keycloak (containerized)
- **Database**: PostgreSQL with Flyway migrations and Spring Data JPA
- **Caching**: Spring Cache with Caffeine (+ optional Redis)
- **Observability**: Micrometer, Prometheus, OpenTelemetry
- **Testing**: JUnit 5, TestContainers, Cucumber
- **Deployment**: Docker containers with GitHub Actions CI/CD
- **Web Frontend**: React with Micro-Frontend architecture
- **API Gateway**: Backend-for-Frontend (BFF) with GraphQL
- **Mobile**: React Native for cross-platform development
- **Compliance**: GDPR framework with Privacy Module
- **Globalization**: Comprehensive i18n/l10n across all platforms

These architectural decisions provide a comprehensive foundation for the Aronim Bookstore application, with a focus on
maintainability, scalability, and developer productivity. The decisions balance modern best practices with practical 
considerations for the specific requirements of the application.
