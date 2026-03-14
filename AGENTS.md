# AGENTS.md - EcommerceApp Developer Guide

This document provides guidelines for agents working on the EcommerceApp codebase.

## Project Overview

- **Type**: Spring Boot 4.0.2 REST API Application
- **Java Version**: 25
- **Build Tool**: Maven
- **Database**: PostgreSQL (JPA)
- **Session Store**: Redis
- **Key Libraries**: Lombok, MapStruct, Spring Security, JWT

## Build & Development Commands

```bash
# Build the project
./mvnw clean install -DskipTests

# Run the application
./mvnw spring-boot:run

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ClassNameTest

# Run a single test method
./mvnw test -Dtest=ClassNameTest#methodName

# Format code (Spring Java Format)
./mvnw spring-javaformat:format

# Validate code format
./mvnw spring-javaformat:validate

# Package without tests
./mvnw package -DskipTests
```

## Code Style Guidelines

### General Conventions

- **Package Structure**: `com.Ecommerce.EcommerceApp.{Layer}`
  - Controllers, Services, Interfaces, Models, DTOs, Repositories, Mappers, Exceptions, Security
- **Spring Java Format**: All code must pass `./mvnw spring-javaformat:validate`
- **Indentation**: 4 spaces (enforced by formatter)

### Naming Conventions

- **Classes**: PascalCase (e.g., `ProductController`, `ProductServiceImpl`)
- **Interfaces**: PascalCase with Service suffix (e.g., `ProductService`)
- **Implementations**: PascalCase with Impl suffix (e.g., `ProductServiceImpl`)
- **Packages**: lowercase (e.g., `controllers`, `dtos`)
- **Variables**: camelCase
- **Constants**: UPPER_SNAKE_CASE
- **DTOs**: EntityName + DTO suffix (e.g., `ProductDto`, `OrderDTO`)
- **Response DTOs**: EntityName + ResponseDto suffix (e.g., `ProductResponseDto`)

### Dependency Injection

- Use constructor injection via `@RequiredArgsConstructor` (Lombok)
- Do not use `@Autowired` on fields
- Services should implement interfaces

```java
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
}
```

### Entity/Model Guidelines

- Use Lombok annotations: `@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`, `@ToString`
- Use JPA annotations: `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@ManyToOne`, `@OneToMany`
- Always implement `Serializable` for entities
- Use `@CreationTimestamp` and `@UpdateTimestamp` for audit fields

```java
@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    // ...
}
```

### DTO Guidelines

- Use separate DTOs for requests and responses when needed
- Use `@Getter`/`@Setter` instead of `@Data` for DTOs to avoid issues with `hashCode`/`equals`
- Implement `Serializable`
- Use validation annotations: `@NotBlank`, `@Size`, `@NotNull`

### Mapper Guidelines

- Use MapStruct for all entity-DTO mappings
- Define mappers as interfaces with `@Mapper(componentModel = "spring")`
- Use `@Mapping` to ignore fields or map differently
- Create `toDto()`, `toEntity()`, and update methods

```java
@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
    List<ProductDto> toDto(List<Product> products);
    
    @Mapping(target = "createdAt", ignore = true)
    Product toEntity(ProductDto dto);
    
    @Mapping(target = "id", ignore = true)
    void updateFromDto(ProductDto dto, @MappingTarget Product entity);
}
```

### Controller Guidelines

- Use `@RestController` with `@RequestMapping`
- Use `@RequiredArgsConstructor` for DI
- Place `@Valid` on `@RequestBody` parameters
- Return `ResponseEntity<T>` from all endpoints
- Use appropriate HTTP status codes: `OK` (200), `CREATED` (201), `NO_CONTENT` (204)
- Group endpoints with path prefixes: `/api/v1/products`, `/api/v1/admin/products`
- Public endpoints go under `/public` sub-path

```java
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/public")
    public ResponseEntity<ProductResponseDto> getProducts(...) {
        return new ResponseEntity<>(productService.getAllProducts(...), HttpStatus.OK);
    }
}
```

### Service Layer

- Define interfaces in `Interfaces` package
- Implement in `Services` package with `Impl` suffix
- Use `@Service` annotation
- Use `@Slf4j` for logging
- Use `@Cacheable`, `@CacheEvict` for caching (Redis)
- Throw specific exceptions (`ResourceNotFoundException`, `ApiException`)

### Exception Handling

- Use `@RestControllerAdvice` for global exception handling
- Create custom exceptions in `Exceptions` package
- Return consistent error response structure via `ApiResponseDto`

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ApiResponseDto(ex.getMessage(), false), HttpStatus.NOT_FOUND);
    }
}
```

### Security Guidelines

- JWT-based authentication
- Use `SecurityConfig` for security configuration
- Configure stateless session: `SessionCreationPolicy.STATELESS`
- Use BCrypt for password encoding
- Public endpoints explicitly permitted, others require authentication

### Validation

- Use Jakarta Validation (`jakarta.validation.constraints.*`)
- Apply `@Valid` on controller method parameters
- Handle `MethodArgumentNotValidException` in global handler

### Import Organization

Group imports in this order (Spring Java Format enforces this):
1. `java.*` / `javax.*`
2. `org.springframework.*`
3. Other third-party imports
4. Project imports (`com.Ecommerce.*`)

### Testing Guidelines

- Place tests in `src/test/java` mirroring main package structure
- Use `@SpringBootTest` for integration tests
- Use `@WebMvcTest` for controller tests
- Use `@DataJpaTest` for repository tests

## Database Conventions

- Use JPA repositories extending `JpaRepository`
- Follow naming: `{Entity}Repository` (e.g., `ProductRepository`)
- Use entity annotations for relationships: `@OneToMany`, `@ManyToOne`, `@JoinColumn`
- Use `CascadeType` carefully - typically `PERSIST`, `MERGE`

## API Design Patterns

- RESTful endpoints: `/api/v1/{resource}`
- Pagination: `pageNumber`, `pageSize` query params
- Sorting: `sortBy`, `sortOrder` query params
- ID in path: `/api/v1/products/{id}`
- Nested resources: `/api/v1/categories/{categoryId}/products`
- Admin-only: `/api/v1/admin/**`
- Public: `/api/v1/public/**` or `/api/v1/{resource}/public/**`

## Configuration

- Use `application.properties` / `application.yml` for configuration
- Environment-specific profiles: `application-{profile}.yml`
- Constants in `Lib/AppConstants.java`

## Notes

- Do NOT commit secrets, credentials, or API keys
- Always run `./mvnw spring-javaformat:format` before committing
- Run tests before pushing code
- Follow existing code patterns in the codebase
