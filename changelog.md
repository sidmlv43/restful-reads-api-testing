# Changelog

## [Unreleased]

### Added

#### Configuration
- Added `ConfigManager` for centralized environment configuration management.
- Added `RestAssuredConfig` for global RestAssured setup.
- Enabled centralized request and response logging through RestAssured filters.

#### Authentication
- Added `AuthService` for authentication-related API operations:
    - Login
    - Register
- Added request models:
    - `LoginRequest`
    - `RegisterRequest`
- Added response model:
    - `AuthToken`
- Added `AuthEndpoints` constants class.

#### User Session Management
- Added `UserType` enum:
    - `ADMIN`
    - `CUSTOMER`
- Added `TokenManager` for storing and retrieving tokens by user type.
- Added thread-safe `SessionManager` using `ThreadLocal` for managing active user sessions during test execution.
- Added support for role-based test execution without repeated authentication.

#### API Models
- Added `BookQueryParams` model using Lombok Builder pattern.
- Added support for:
    - Pagination
    - Sorting
    - Author filtering
    - Minimum rating filtering
    - Dynamic filter map support

#### Endpoint Management
- Introduced endpoint constant classes:
    - `AuthEndpoints`
    - `BookEndpoints`
- Moved endpoint strings out of service implementations.

#### Assertions
- Added assertion layer to separate validation logic from tests.
- Added authentication assertions:
    - Token validation
- Added book-related assertions:
    - Numeric value comparison validation (`>=` checks)

#### Testing Framework
- Added `BaseTest`.
- Planned suite-level authentication initialization via `@BeforeSuite`.
- Added reusable authentication flow for test setup.

---

### Changed

#### AuthService
- Refactored login method to accept `LoginRequest` instead of raw email/password parameters.
- Refactored register method to accept `RegisterRequest`.
- Added explicit JSON content type for request bodies.
- Removed direct session manipulation from authentication methods.
- Authentication methods now return tokens and delegate session/token handling to framework components.

#### Session Design
- Reworked authentication flow:
    - Authentication responsibility stays inside `AuthService`.
    - Token storage responsibility moved to `TokenManager`.
    - Active-user selection responsibility moved to `SessionManager`.

#### Query Parameter Design
- Enhanced `BookQueryParams` to support advanced operator-based filtering.

Examples:

```java
.filters(Map.of(
    "price[gte]", 10,
    "price[lte]", 100
))