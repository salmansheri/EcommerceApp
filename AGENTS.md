# AGENTS.md - Guidelines for Coding Agents

## Build/Lint/Test Commands
- Build: `mvn clean install`
- Lint: No dedicated linter; rely on compiler warnings (`mvn compile`)
- Run all tests: `mvn test`
- Run single test: `mvn test -Dtest=ClassName#testMethod` (e.g., `mvn test -Dtest=EcommerceAppApplicationTests#contextLoads`)

## Code Style Guidelines
- **Imports**: Group Spring/framework imports first, then custom (com.Ecommerce.*).
- **Naming**: PascalCase for classes/interfaces (e.g., ProductServiceImpl), camelCase for methods/variables (e.g., getAllProducts), interfaces start with 'I' (e.g., ICategoryService).
- **Formatting**: 4-space indentation, annotations on separate lines, use Lombok (@Data, @AllArgsConstructor, etc.) for boilerplate.
- **Types**: Use generics (e.g., List<ProductDto>), add validation (@NotBlank, @Size) on entities/DTOs.
- **Error Handling**: Throw custom exceptions (ApiException, ResourceNotFoundException); handle via GlobalExceptionHandler.
- **Patterns**: Use MapStruct for DTO-Entity mappings, follow REST API conventions (/api paths, pagination with pageNumber/pageSize).
- **Other**: Prefer constructor injection, add timestamps (@CreationTimestamp, @UpdateTimestamp) on entities.