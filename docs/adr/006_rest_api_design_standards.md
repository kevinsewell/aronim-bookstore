# ADR-005: REST API Design Standards for Spring Boot Application

## Status
Accepted

## Date
2025-03-01

## Context
Our Spring Boot application will expose REST APIs to be consumed by various clients including web applications, mobile apps, and potentially third-party integrations. To ensure consistency, predictability, and maintainability of our APIs, we need to establish clear standards for:

- Resource naming and URL structure
- HTTP methods usage
- Request/response formats
- Error handling
- Versioning strategy
- Pagination, filtering, and sorting
- Authentication and authorization
- Documentation

Without established standards, our API design might become inconsistent across different parts of the application, leading to a confusing developer experience, maintenance challenges, and potential breaking changes.

## Decision
We will implement the following REST API design standards:

### 1. Resource Naming and URL Structure
- Use nouns, not verbs, for resource names (e.g., `/products`, not `/getProducts`)
- Use plural nouns for collection resources (e.g., `/customers` not `/customer`)
- Use kebab-case for multi-word resource names (e.g., `/order-items`)
- Use hierarchical structure for related resources (e.g., `/customers/{id}/addresses`)
- Limit resource nesting to maximum 2 levels to avoid complexity
- Keep URLs as short and meaningful as possible

### 2. HTTP Methods
- Use standard HTTP methods according to their semantic meaning:
  - `GET`: Retrieve resources (read-only, idempotent)
  - `POST`: Create new resources or trigger operations that change state
  - `PUT`: Replace resources completely (idempotent)
  - `PATCH`: Partial update of resources
  - `DELETE`: Remove resources (idempotent)
- Do not use request bodies with `GET` requests
- Include resource ID in the URL for `PUT`, `PATCH`, and `DELETE` operations

### 3. Request/Response Format
- Use JSON as the primary data exchange format
- Use consistent field naming convention: camelCase for all JSON properties
- Include a root element for collections: `{"items": [...], "metadata": {...}}`
- For date/time values, use ISO 8601 format (e.g., `2023-04-15T14:30:00Z`)
- For empty responses, return HTTP 204 (No Content) with no body

### 4. HTTP Status Codes
- Use appropriate HTTP status codes:
  - `200 OK`: Successful request
  - `201 Created`: Resource successfully created
  - `204 No Content`: Successful request with no response body
  - `400 Bad Request`: Invalid input, validation errors
  - `401 Unauthorized`: Authentication required
  - `403 Forbidden`: Authenticated but not authorized
  - `404 Not Found`: Resource not found
  - `409 Conflict`: Request conflicts with current state
  - `422 Unprocessable Entity`: Semantic validation errors
  - `500 Internal Server Error`: Unexpected server error

### 5. Error Handling
- Use a consistent error response structure:
  ```json
  {
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid input data",
    "details": [
      {
        "field": "email",
        "message": "Must be a valid email address"
      }
    ],
    "timestamp": "2023-04-15T14:30:00Z",
    "path": "/api/v1/customers"
  }
  ```
- Include validation errors with field references when applicable
- Do not expose sensitive information or stack traces in error responses

### 6. Versioning
- Use URI path versioning (e.g., `/api/v1/customers`)
- Increment major version number only for breaking changes
- Maintain at least one previous version when introducing breaking changes
- Document deprecation timeline for outdated versions

### 7. Pagination, Filtering, and Sorting
- Use query parameters for pagination: `?page=0&size=20`
- Include pagination metadata in responses:
  ```json
  {
    "items": [...],
    "metadata": {
      "page": 0,
      "size": 20,
      "totalElements": 243,
      "totalPages": 13
    }
  }
  ```
- Use query parameters for filtering: `?status=active&category=books`
- Use query parameters for sorting: `?sort=lastName,asc&sort=createdDate,desc`
- Document all available filter and sort options

### 8. Authentication and Authorization
- Use OAuth 2.0 / JWT for authentication
- Include `Authorization` header with Bearer token
- Return `401 Unauthorized` for missing or invalid authentication
- Return `403 Forbidden` for authorization failures
- Document security requirements for each endpoint

### 9. Documentation
- Use OpenAPI/Swagger for API documentation
- Document all endpoints, parameters, request/response schemas
- Include examples for request/response payloads
- Document error responses and codes

## Consequences

### Positive
- Consistent API design across the application
- Improved developer experience for API consumers
- Easier onboarding for new team members
- Better maintainability and evolvability of the API
- Reduced risk of breaking changes
- Clearer separation between resources and operations
- Alignment with REST principles and industry best practices
- Improved testability through predictable patterns

### Negative
- May require additional effort to conform to standards
- Some complex operations might be challenging to model in a RESTful way
- Versioning strategy may lead to code duplication during transition periods
- Standards may need to evolve as requirements change

## Alternatives Considered

1. **GraphQL**
   - Would provide more flexibility for clients to request exactly the data they need
   - Better support for complex queries and data relationships
   - Requires different tooling and expertise
   - Less standardized than REST for certain operations
   - Would introduce a steeper learning curve for some team members

2. **RPC-style APIs**
   - More direct mapping to method calls
   - Potentially simpler for certain operation-centric (vs. resource-centric) use cases
   - Less alignment with web architecture principles
   - Typically less cacheable and less discoverable

3. **Content Negotiation Versioning**
   - Using headers like `Accept: application/vnd.company.app-v2+json`
   - Keeps URLs cleaner
   - More complex to test and document
   - Less visible/obvious versioning

## Implementation Notes
- We will implement these standards using Spring MVC controllers
- ResponseEntityExceptionHandler will be extended to provide consistent error responses
- Spring Data's Pageable will be used for pagination implementation
- SpringDoc/OpenAPI will be used for API documentation
- A custom annotation-based validation framework will provide standardized validation errors
- API controllers will be in a separate package from web controllers

## Compliance Verification
- Code reviews will check for adherence to these standards
- API tests will verify correct status codes and response formats
- OpenAPI documentation will be generated and reviewed
- We will create an API design linting tool to automate checks where possible

## References
- RESTful API Design Best Practices: https://restfulapi.net/
- Microsoft REST API Guidelines: https://github.com/microsoft/api-guidelines/blob/vNext/Guidelines.md
- Spring REST API Documentation: https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-controller
