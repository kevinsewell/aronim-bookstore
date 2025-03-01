# ADR-012: Adoption of Spring Data JPA

## Status
Accepted

## Date
[Current Date]

## Context
Our application requires a robust and efficient data access layer to interact with the PostgreSQL database. We need an approach that:

- Reduces boilerplate code for common database operations
- Provides a consistent programming model for data access
- Supports complex queries and data relationships
- Integrates well with our Spring Boot architecture
- Offers good performance and optimization capabilities
- Enables testing of data access logic
- Supports database independence (if needed in the future)
- Manages database transactions effectively
- Balances abstraction with control over SQL when needed
- Aligns with our team's skills and development practices

Implementing a data access layer from scratch using plain JDBC or manually configuring ORM tools would require significant effort and could lead to inconsistent implementations across the application.

## Decision
We will use **Spring Data JPA** as our primary data access technology with Hibernate as the underlying JPA provider. Spring Data JPA will be used to:

### 1. Core Repository Implementation
- Define repository interfaces with standard CRUD operations
- Use derived query methods for simple queries
- Implement custom query methods using JPQL or native SQL
- Leverage Spring Data's pagination and sorting support
- Utilize query by example and specification APIs for dynamic queries

### 2. Entity Mapping and Relationships
- Map domain objects to database tables using JPA annotations
- Define relationships between entities (One-to-One, One-to-Many, Many-to-Many)
- Configure cascading behavior for related entities
- Use appropriate fetch strategies (eager vs. lazy loading)
- Implement optimistic locking for concurrency control

### 3. Transaction Management
- Use Spring's declarative transaction management
- Define transaction boundaries at the service layer
- Configure appropriate isolation levels for specific use cases
- Implement retry logic for transient failures when appropriate

### 4. Advanced Features
- Implement auditing using Spring Data's auditing support
- Use Specification API for complex dynamic queries
- Leverage QueryDSL integration for type-safe queries when needed
- Implement custom repository methods for specific requirements
- Use projections for optimized data retrieval

## Implementation Approach

1. **Repository Structure**
   - Create repository interfaces extending Spring Data's `JpaRepository`
   - Organize repositories by domain entity
   - Implement custom repository interfaces and implementations when needed

2. **Entity Design**
   - Follow best practices for entity design (identity, equality, immutability)
   - Use appropriate JPA annotations for mapping
   - Implement proper equals/hashCode methods
   - Use validation annotations for entity validation

3. **Query Optimization**
   - Use projections for read operations that don't need full entities
   - Configure appropriate fetch strategies to avoid N+1 query problems
   - Use batch operations for bulk updates and deletes
   - Monitor and optimize query performance

4. **Testing Strategy**
   - Use Spring Boot test slices for repository testing
   - Implement integration tests with test containers
   - Use appropriate test data setup and cleanup

## Consequences

### Positive
- Significant reduction in boilerplate data access code
- Consistent approach to data access across the application
- Leverages Spring ecosystem integration
- Declarative transaction management
- Database vendor independence through JPA abstraction
- Built-in support for pagination and sorting
- Extensible through custom implementations
- Strong community support and documentation
- Familiar to many Java developers
- Supports both simple CRUD and complex query scenarios
- Integration with validation framework

### Negative
- Learning curve for JPA concepts and annotations
- Potential for performance issues if not used properly
- Risk of N+1 query problems if relationships not managed correctly
- Abstraction can hide SQL complexity in some cases
- Potential mismatch between object model and relational model
- Some complex queries may be difficult to express in JPA
- Hibernate's proxy objects can cause unexpected behavior
- Entity state management can be complex in some scenarios
- May lead to excessive memory usage with large result sets

## Alternatives Considered

1. **Spring JDBC Template**
   - More direct control over SQL
   - Lower abstraction level
   - Less boilerplate than raw JDBC
   - More verbose than Spring Data JPA
   - Manual mapping between result sets and objects
   - No ORM capabilities

2. **MyBatis**
   - SQL-centric approach
   - Direct control over queries
   - Good for complex queries and stored procedures
   - Less abstraction for common operations
   - Manual mapping configuration
   - Less integration with Spring ecosystem

3. **jOOQ**
   - Type-safe SQL construction
   - Better SQL visibility and control
   - Strong integration with database-specific features
   - Less abstraction for entity relationships
   - Learning curve for DSL
   - Additional build-time code generation

4. **Custom Repository Implementation**
   - Tailored exactly to our needs
   - Full control over implementation details
   - Significant development and maintenance effort
   - Risk of inconsistent implementations
   - No community support

## Implementation Notes

### Basic Repository Example

```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    
    // Derived query method
    Optional<Customer> findByEmail(String email);
    
    // Query method with JPQL
    @Query("SELECT c FROM Customer c WHERE c.lastName LIKE %:lastName%")
    List<Customer> findByLastNameContaining(@Param("lastName") String lastName);
    
    // Native query example
    @Query(value = "SELECT * FROM customers WHERE created_at > :date", nativeQuery = true)
    List<Customer> findNewCustomers(@Param("date") LocalDateTime date);
    
    // Pagination example
    Page<Customer> findByLastNameStartingWith(String lastNamePrefix, Pageable pageable);
    
    // Count query
    long countByStatus(CustomerStatus status);
}
```

### Entity Class Example

```java
@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "name", nullable = false)
    @NotBlank(message = "Product name is required")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "price", nullable = false)
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductAttribute> attributes = new HashSet<>();
    
    @Version
    private Long version;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
    
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
    
    // Business methods
    public void addAttribute(String name, String value) {
        ProductAttribute attribute = new ProductAttribute(name, value);
        attributes.add(attribute);
        attribute.setProduct(this);
    }
    
    public void removeAttribute(ProductAttribute attribute) {
        attributes.remove(attribute);
        attribute.setProduct(null);
    }
    
    // Equals and hashCode based on business key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id != null && id.equals(product.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
```

### Service Layer with Transaction Management

```java
@Service
@Transactional(readOnly = true)
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    
    public ProductService(ProductRepository productRepository, 
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    
    public List<ProductDTO> findAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<ProductDTO> findProductById(UUID id) {
        return productRepository.findById(id)
                .map(this::convertToDto);
    }
    
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        updateProductFromDto(product, productDTO);
        
        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }
    
    @Transactional
    public Optional<ProductDTO> updateProduct(UUID id, ProductDTO productDTO) {
        return productRepository.findById(id)
                .map(product -> {
                    updateProductFromDto(product, productDTO);
                    return convertToDto(productRepository.save(product));
                });
    }
    
    @Transactional
    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
    
    // Using Specification API for dynamic queries
    public Page<ProductDTO> findProducts(ProductCriteria criteria, Pageable pageable) {
        Specification<Product> spec = Specification.where(null);
        
        if (criteria.getMinPrice() != null) {
            spec = spec.and((root, query, cb) -> 
                cb.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
        }
        
        if (criteria.getMaxPrice() != null) {
            spec = spec.and((root, query, cb) -> 
                cb.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
        }
        
        if (StringUtils.hasText(criteria.getCategoryName())) {
            spec = spec.and((root, query, cb) -> {
                Join<Product, Category> categoryJoin = root.join("category");
                return cb.like(cb.lower(categoryJoin.get("name")), 
                    "%" + criteria.getCategoryName().toLowerCase() + "%");
            });
        }
        
        return productRepository.findAll(spec, pageable)
                .map(this::convertToDto);
    }
    
    // Helper methods
    private ProductDTO convertToDto(Product product) {
        // Mapping logic
    }
    
    private void updateProductFromDto(Product product, ProductDTO dto) {
        // Update logic
    }
}
```

### Spring Boot Configuration

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        jdbc:
          batch_size: 25
        order_inserts: true
        order_updates: true
        fetch.size: 100
        default_batch_fetch_size: 100
    open-in-view: false
```

## Testing Example

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class CustomerRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldFindCustomerByEmail() {
        // Given
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customerRepository.save(customer);

        // When
        Optional<Customer> found = customerRepository.findByEmail("john.doe@example.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John");
        assertThat(found.get().getLastName()).isEqualTo("Doe");
    }
}
```

## Compliance Verification
- Code reviews to ensure proper use of JPA annotations and relationships
- Performance testing of complex queries
- Static analysis to detect common JPA pitfalls
- Integration tests for repository methods
- Monitoring of query execution times in development and production
- Regular review of Hibernate statistics
- Database schema validation on application startup

## References
- Spring Data JPA Documentation: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
- Hibernate ORM Documentation: https://hibernate.org/orm/documentation/
- JPA Specification: https://jakarta.ee/specifications/persistence/
- Spring Data JPA Query Methods: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
- Vlad Mihalcea's Hibernate Blog: https://vladmihalcea.com/tutorials/hibernate/
- Java Persistence with Hibernate (Book): https://www.manning.com/books/java-persistence-with-hibernate
- Spring Boot Testing: https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing
