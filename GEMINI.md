# Gemini Context: EcommerceApp

## Project Overview
**EcommerceApp** is a backend E-commerce REST API built with **Java 25** and **Spring Boot**. It manages users, products, categories, carts, and orders, utilizing **PostgreSQL** for persistence and **Redis** for caching and session management. Security is handled via **JWT** (JSON Web Tokens).

## Tech Stack
- **Language:** Java 25
- **Framework:** Spring Boot 3.x (Parent 4.0.0 in pom? Likely Spring Boot 3.4+ or 4.0 snapshot)
- **Build Tool:** Maven
- **Database:** PostgreSQL
- **Caching:** Redis
- **Security:** Spring Security + JWT (JJWT library)
- **Utilities:** Lombok, MapStruct, Docker Compose

## Architecture & Structure
The project follows a standard layered architecture:
- **`src/main/java/com/Ecommerce/EcommerceApp/`**
  - **`Controllers/`**: REST Endpoints.
  - **`Services/`**: Business logic implementations.
  - **`Interfaces/`**: Service interfaces (naming convention: `I{ServiceName}`).
  - **`Repositories/`**: JPA Repositories.
  - **`Models/`**: JPA Entities (Database tables).
  - **`Dtos/`**: Data Transfer Objects for API requests/responses.
  - **`Mappers/`**: MapStruct mappers for Entity <-> DTO conversion.
  - **`Exceptions/`**: Global exception handling (`GlobalExceptionHandler`) and custom exceptions.
  - **`Security/`**: JWT filters, configuration, and user details services.
  - **`config/`**: App-wide configs (e.g., Redis).

## Key Commands

### Build & Run
- **Build Project:**
  ```bash
  ./mvnw clean install
  ```
- **Run Application:**
  ```bash
  ./mvnw spring-boot:run
  ```

### Infrastructure (Docker)
- **Start Database & Redis:**
  ```bash
  docker-compose up -d
  ```
  *Ensure ports 5432 (Postgres) and 6379 (Redis) are free.*

### Testing
- **Run All Tests:**
  ```bash
  ./mvnw test
  ```
- **Run Specific Test:**
  ```bash
  ./mvnw test -Dtest=ClassName#methodName
  ```

## Development Conventions
- **Code Style:**
  - **Naming:** `PascalCase` for classes, `camelCase` for methods/variables. Interfaces often prefixed with `I` (e.g., `ICategoryService`).
  - **Lombok:** Heavily used (`@Data`, `@AllArgsConstructor`, `@Builder`) to reduce boilerplate.
  - **Mapping:** Use **MapStruct** for all DTO <-> Entity mappings. Avoid manual setting where possible.
  - **Injection:** Prefer Constructor Injection (often via `@RequiredArgsConstructor`).
- **Configuration:**
  - Properties located in `src/main/resources/application.properties`.
  - Database credentials currently set to local defaults (`postgres`/`salman`).
- **Security:**
  - Stateless JWT authentication.
  - Token logic in `Security/Jwt/JwtUtils.java`.
- **Error Handling:**
  - Throw specific exceptions (e.g., `ResourceNotFoundException`, `ApiException`).
  - Handled centrally in `GlobalExceptionHandler` returning `ApiResponseDto`.

## Configuration Details
- **Database URL:** `jdbc:postgresql://localhost:5432/ecommercedb`
- **Redis Host:** `localhost:6379`
- **Java Version:** 25 (Ensure JDK 25 is installed or configured in toolchain)
