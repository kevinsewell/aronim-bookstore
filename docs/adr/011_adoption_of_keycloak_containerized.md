# ADR-011: Adoption of Keycloak for Identity Management (Containerized Deployment)

## Status
Rejected

## Date
2025-03-01

## Context
Our application requires a robust identity and access management (IAM) solution to handle authentication, authorization, and user management. We need a system that:

- Provides secure authentication for our web and mobile applications
- Supports modern authentication protocols (OAuth 2.0, OpenID Connect)
- Offers comprehensive user management capabilities
- Can integrate with existing enterprise identity providers
- Supports single sign-on (SSO) across multiple applications
- Provides robust security features and compliance capabilities
- Can be self-hosted or used as a managed service
- Integrates well with our Spring Boot application architecture
- Offers flexibility for future growth and changing requirements

Building these capabilities from scratch would be time-consuming, risky, and would divert resources from our core business functionality. Therefore, we need to select an established IAM solution that meets our requirements.

## Decision
We will adopt **Keycloak** as our identity and access management solution. Keycloak is an open-source identity and access management solution developed by Red Hat that provides:

### 1. Core Identity Management Features
- User registration and profile management
- Password policies and recovery workflows
- Multi-factor authentication (MFA)
- Session management
- Account linking and social login
- User federation with LDAP/Active Directory

### 2. Authentication & Authorization
- OAuth 2.0 implementation (all standard flows)
- OpenID Connect support
- Fine-grained role-based access control (RBAC)
- Attribute-based access control (ABAC)
- Client access management
- Token customization

### 3. Integration Capabilities
- Well-documented REST APIs
- SAML 2.0 support for enterprise integration
- Identity brokering with external providers
- Spring Security integration
- Client adapters for various platforms

### 4. Administration & Security
- Administrative console for user management
- Audit logging
- Brute force detection and prevention
- Account threat detection
- Custom authentication flows

### 5. Deployment Options
- Self-hosted (on-premises or cloud infrastructure)
- Container-friendly architecture
- Clustering support for high availability
- Database flexibility (supports multiple backends)

## Implementation Approach
We will implement Keycloak with the following approach:

1. **Deployment Strategy**
   - Initially deploy Keycloak in a containerized environment
   - Configure with PostgreSQL as the backend database
   - Set up with redundancy for high availability

2. **Integration with Our Application**
   - Use Spring Security OAuth2 Resource Server for API protection
   - Implement OpenID Connect for web application authentication
   - Configure client applications in Keycloak

3. **User Management**
   - Define initial roles and groups structure
   - Configure password policies and MFA requirements
   - Set up self-service registration and profile management

4. **Custom Theming**
   - Customize login pages to match our application branding
   - Ensure consistent user experience across authentication flows

5. **Monitoring and Operations**
   - Implement monitoring for Keycloak instances
   - Set up alerting for authentication anomalies
   - Establish backup and recovery procedures

## Consequences

### Positive
- Comprehensive IAM solution without building from scratch
- Industry-standard security protocols and best practices
- Reduced development time for authentication features
- Flexibility to adapt to changing security requirements
- Centralized user management across multiple applications
- Strong community support and regular security updates
- Ability to federate with enterprise identity providers
- Self-hosted option maintains control over sensitive data
- Open-source with commercial support options available

### Negative
- Introduces additional infrastructure to maintain
- Learning curve for team to understand Keycloak configuration
- Potential performance overhead for authentication flows
- Requires expertise for proper security configuration
- Customization may be complex for advanced scenarios
- Upgrade management for security patches
- Potential single point of failure if not properly clustered
- Additional monitoring and operational requirements

## Alternatives Considered

1. **Auth0**
   - Fully managed authentication service
   - Excellent developer experience
   - Higher cost for growing user base
   - Less control over data and infrastructure
   - Potential vendor lock-in

2. **Okta**
   - Enterprise-focused identity solution
   - Comprehensive features and compliance
   - Significant cost for growing organizations
   - More complex than needed for our current scale
   - Less flexibility for customization

3. **Custom Implementation with Spring Security**
   - Complete control over implementation
   - Tailored to our exact requirements
   - Significantly higher development and maintenance effort
   - Higher security risk of custom implementation
   - Diversion of resources from core business functionality

4. **Firebase Authentication**
   - Easy integration for mobile applications
   - Limited customization options
   - Tied to Google Cloud ecosystem
   - Less suitable for enterprise integration
   - Limited administrative capabilities

## Implementation Notes

### Spring Boot Integration

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers("/api/v*/public/**").permitAll()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());
    }
    
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("realm_access.roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
```

### Application Properties Configuration

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://keycloak.example.com/auth/realms/our-application
          jwk-set-uri: https://keycloak.example.com/auth/realms/our-application/protocol/openid-connect/certs
```

### Keycloak Docker Deployment

```yaml
version: '3'

services:
  postgres:
    image: postgres:13
    volumes:
      - postgres_data:/var/lib/postgresql/data
    environment:
      POSTGRES_DB: keycloak
      POSTGRES_USER: keycloak
      POSTGRES_PASSWORD: password
    networks:
      - keycloak-network

  keycloak:
    image: quay.io/keycloak/keycloak:latest
    environment:
      DB_VENDOR: POSTGRES
      DB_ADDR: postgres
      DB_DATABASE: keycloak
      DB_USER: keycloak
      DB_PASSWORD: password
      KEYCLOAK_USER: admin
      KEYCLOAK_PASSWORD: admin_password
      PROXY_ADDRESS_FORWARDING: 'true'
    ports:
      - 8080:8080
    depends_on:
      - postgres
    networks:
      - keycloak-network

volumes:
  postgres_data:

networks:
  keycloak-network:
```

## Compliance Verification
- Regular security audits of Keycloak configuration
- Penetration testing of authentication flows
- Monitoring of authentication events and anomalies
- Regular reviews of access policies and permissions
- Verification of compliance requirements (GDPR, HIPAA, etc.)
- Testing of disaster recovery procedures
- Validation of integration with application security

## References
- Keycloak Official Documentation: https://www.keycloak.org/documentation
- Spring Security OAuth2 Resource Server: https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html
- Keycloak with Spring Boot: https://www.baeldung.com/spring-boot-keycloak
- Securing Spring Boot with Keycloak: https://developers.redhat.com/blog/2017/05/25/easily-secure-your-spring-boot-applications-with-keycloak
- Keycloak Clustering Guide: https://www.keycloak.org/docs/latest/server_installation/#_clustering
