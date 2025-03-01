# ADR-013: Adoption of PostgreSQL as Primary Database

## Status
Accepted

## Date
2025-03-01

## Context
Our application requires a reliable, performant, and feature-rich database system to store and manage data. We need a database solution that:

- Provides robust ACID compliance for data integrity
- Offers excellent performance for our expected workload
- Supports complex queries and data relationships
- Has strong security features
- Scales well as our data and user base grow
- Integrates well with our Spring Boot application
- Supports advanced data types and operations
- Offers good tooling and community support
- Provides enterprise-grade reliability features
- Aligns with our operational capabilities and constraints

Selecting the right database technology is critical as it impacts application performance, data integrity, development productivity, and operational complexity. This decision has long-term implications as changing database systems later would require significant migration effort.

## Decision
We will use **PostgreSQL** as the primary relational database management system (RDBMS) for our application.

### 1. Core Database Features
- Full ACID compliance for transaction reliability
- Robust relational model with referential integrity
- Rich set of data types (including JSON, arrays, geometric types)
- Advanced indexing capabilities (B-tree, Hash, GiST, GIN, etc.)
- Powerful query optimizer
- Full-text search capabilities
- Support for stored procedures and functions
- Triggers and rules for automated actions
- Table partitioning for large datasets
- Mature replication features

### 2. Extensions and Advanced Features
- PostGIS for geospatial data and queries
- Full-text search with language support
- JSON/JSONB support for semi-structured data
- Foreign data wrappers for connecting to external data sources
- Custom data types and operators
- Materialized views for query optimization
- Logical replication for selective data replication
- Row-level security for fine-grained access control
- Native UUID support

### 3. Integration with Spring Boot
- Use Spring Data JPA with Hibernate as the ORM layer
- Configure connection pooling with HikariCP
- Implement database migrations with Flyway
- Utilize Spring's transaction management

### 4. Operational Approach
- Deploy PostgreSQL in a managed cloud service where appropriate
- Implement regular backup and point-in-time recovery
- Configure high availability with replication
- Monitor performance with appropriate tools
- Follow PostgreSQL best practices for configuration

## Consequences

### Positive
- Strong data integrity guarantees through ACID compliance
- Excellent support for complex data models and relationships
- Rich ecosystem of tools and extensions
- Strong community and commercial support options
- Open-source with no licensing costs
- Enterprise-grade features without enterprise pricing
- Excellent documentation and learning resources
- Good performance for both read and write operations
- Ability to handle structured and semi-structured data
- Well-supported by Spring ecosystem
- Proven track record in production environments
- Active development and regular security updates

### Negative
- Requires database administration expertise
- May have higher operational complexity than some NoSQL alternatives
- Potentially higher initial setup effort for replication and high availability
- Vertical scaling limitations compared to some distributed databases
- Learning curve for advanced features and optimization
- Connection management needs careful configuration for optimal performance
- Requires proper indexing strategy to maintain performance as data grows
- Memory requirements can be significant for optimal performance

## Alternatives Considered

1. **MySQL/MariaDB**
   - Widely used and familiar to many developers
   - Good performance for read-heavy workloads
   - Less feature-rich compared to PostgreSQL
   - Some concerns about Oracle's stewardship of MySQL
   - Different transaction isolation model
   - Less advanced query optimization

2. **Oracle Database**
   - Enterprise-grade features and support
   - Excellent performance and scalability
   - High licensing costs
   - Vendor lock-in concerns
   - Complex licensing model
   - Steeper learning curve for development teams

3. **Microsoft SQL Server**
   - Strong integration with Microsoft ecosystem
   - Good developer tools
   - Licensing costs for full feature set
   - Less native support in some cloud environments
   - Platform limitations for deployment

4. **MongoDB**
   - Schema flexibility for document data
   - Potentially simpler scaling for some workloads
   - Less mature transaction support
   - Different data modeling approach
   - Not ideal for complex relationships and joins
   - Potentially more complex data consistency guarantees

5. **Cloud-Native Options (e.g., Amazon Aurora, Google Cloud Spanner)**
   - Managed services with less operational overhead
   - Excellent scalability
   - Potential vendor lock-in
   - Higher costs at scale
   - Less control over configuration

## Implementation Notes

### Spring Boot Configuration

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:appdb}
    username: ${DB_USERNAME:appuser}
    password: ${DB_PASSWORD:apppassword}
    driver-class-name: org.postgresql.Driver
    hikari:
      minimum-idle: 5
      maximum-pool-size: 20
      idle-timeout: 30000
      max-lifetime: 2000000
      connection-timeout: 30000
      pool-name: HikariPool
      
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        format_sql: true
        jdbc:
          lob:
            non_contextual_creation: true
        temp:
          use_jdbc_metadata_defaults: false
    open-in-view: false
    
  flyway:
    enabled: true
    baseline-on-migrate: true
    locations: classpath:db/migration
    schemas: public
```

### Database Migration Example (Flyway)

```sql
-- V1__Initial_schema.sql
CREATE TABLE customers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE addresses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    street VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50),
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(50) NOT NULL,
    address_type VARCHAR(20) NOT NULL,
    is_default BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_addresses_customer_id ON addresses(customer_id);
```

### Entity Example

```java
@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses = new HashSet<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;
    
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;
    
    // Methods to manage bidirectional relationship
    public void addAddress(Address address) {
        addresses.add(address);
        address.setCustomer(this);
    }
    
    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setCustomer(null);
    }
}
```

### Repository Example

```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    
    Optional<Customer> findByEmail(String email);
    
    List<Customer> findByLastNameContainingIgnoreCase(String lastNamePart);
    
    @Query("SELECT c FROM Customer c JOIN c.addresses a WHERE a.country = :country")
    List<Customer> findByAddressCountry(@Param("country") String country);
    
    boolean existsByEmail(String email);
}
```

### PostgreSQL Specific Features Usage

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    
    // Using PostgreSQL full-text search
    @Query(value = "SELECT * FROM products WHERE to_tsvector('english', name || ' ' || description) @@ to_tsquery('english', :searchTerm)", nativeQuery = true)
    List<Product> fullTextSearch(@Param("searchTerm") String searchTerm);
    
    // Using PostgreSQL JSON operations
    @Query(value = "SELECT * FROM products WHERE properties ->> 'color' = :color", nativeQuery = true)
    List<Product> findByColor(@Param("color") String color);
    
    // Using PostgreSQL array operations
    @Query(value = "SELECT * FROM products WHERE :tag = ANY(tags)", nativeQuery = true)
    List<Product> findByTag(@Param("tag") String tag);
}
```

## Deployment Considerations

### Docker Compose Example

```yaml
version: '3'

services:
  postgres:
    image: postgres:14
    environment:
      POSTGRES_DB: appdb
      POSTGRES_USER: appuser
      POSTGRES_PASSWORD: apppassword
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    command: postgres -c max_connections=200 -c shared_buffers=512MB -c effective_cache_size=1536MB -c work_mem=2621kB -c maintenance_work_mem=128MB

volumes:
  postgres_data:
```

### Production Configuration Considerations
- Connection pooling properly sized for workload
- Appropriate memory allocation for PostgreSQL
- Regular VACUUM and analyze for performance
- Proper indexing strategy based on query patterns
- Point-in-time recovery configuration
- High availability setup with replication
- Monitoring and alerting for database health
- Regular backups with validation

## Compliance Verification
- Performance testing with realistic data volumes
- Verification of backup and restore procedures
- Testing of failover scenarios for high availability
- Security audits of database configuration
- Validation of data integrity constraints
- Monitoring of query performance
- Regular review of indexing strategy

## References
- PostgreSQL Official Documentation: https://www.postgresql.org/docs/
- Spring Data JPA with PostgreSQL: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
- PostgreSQL Performance Tuning: https://wiki.postgresql.org/wiki/Performance_Optimization
- Flyway Database Migrations: https://flywaydb.org/documentation/
- PostgreSQL High Availability: https://www.postgresql.org/docs/current/high-availability.html
- PostgreSQL JSON Support: https://www.postgresql.org/docs/current/datatype-json.html
