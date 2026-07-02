# Restful Reads API Automation Framework

A scalable API automation framework built using Java, TestNG, Rest Assured, and Lombok for testing the Restful Reads application.

The primary goal of this project is to learn and apply automation architecture principles, framework design, test engineering, and API automation best practices while progressing toward Senior SDET and Test Management responsibilities.

---

# Tech Stack

- Java 17
- Maven
- TestNG
- Rest Assured
- Lombok
- Jackson

### Planned

- Extent Reports
- Java Faker
- Docker
- Jenkins
- GitHub Actions / Azure DevOps

---

# Project Structure

```text
src
├── main
│   └── java
│       └── com.restfullReads
│           ├── annotations
│           ├── config
│           ├── constants
│           ├── enums
│           ├── models
│           ├── services
│           └── session
│
└── test
    └── java
        └── com.restfullReads
            ├── assertions
            ├── base
            ├── listeners
            └── tests
```

---

# Framework Features

## Configuration Management

Application configuration is centralized through `ConfigManager`.

Example:

```java
ConfigManager.getBaseUrl();
```

Benefits:

- Centralized configuration
- Easier environment management
- Better maintainability

---

# Endpoint Management

API routes are maintained separately from service implementations.

Example:

```java
AuthEndPoints.LOGIN
AuthEndPoints.REGISTER

BookEndpoints.BASE
```

Benefits:

- Avoids hardcoded URLs
- Easier endpoint updates
- Better code organization

---

# Authentication

## AuthService

Provides methods for:

- Login
- Registration

### Login Example

```java
String token =
        authService.login(
                LoginRequest.builder()
                        .email("admin@example.com")
                        .password("adminpass")
                        .build()
        );
```

### Register Example

```java
String token =
        authService.register(
                RegisterRequest.builder()
                        .name("John Doe")
                        .email("john@example.com")
                        .password("password123")
                        .build()
        );
```

---

# Authentication Models

## LoginRequest

```java
LoginRequest.builder()
        .email("user@example.com")
        .password("password123")
        .build();
```

---

## RegisterRequest

```java
RegisterRequest.builder()
        .name("John Doe")
        .email("john@example.com")
        .password("password123")
        .build();
```

---

## AuthToken

Represents authentication responses.

Example:

```json
{
  "token": "<jwt-token>"
}
```

---

# Role-Based User Management

## UserType

```java
public enum UserType {

    ADMIN,
    CUSTOMER

}
```

The framework currently supports:

- Admin users
- Customer users

---

# TokenManager

Stores authentication tokens for known user types.

Example:

```java
TokenManager.register(
        UserType.ADMIN,
        adminToken
);

TokenManager.register(
        UserType.CUSTOMER,
        customerToken
);
```

Tokens are loaded once and reused across the entire test suite.

Benefits:

- Faster execution
- Fewer login calls
- Reduced API traffic

---

# SessionManager

SessionManager maintains the active user session for the current test thread.

Example:

```java
SessionManager.use(UserType.ADMIN);
```

Internally:

```text
ADMIN
    ↓
TokenManager
    ↓
SessionManager
    ↓
Request Authorization Header
```

The implementation is thread-safe and supports future parallel execution.

---

# User Annotation

## @UseUser

Tests can declare the required user role through annotation-based configuration.

Example:

```java
@Test
@UseUser(UserType.ADMIN)
public void adminShouldCreateBook() {

}
```

Benefits:

- Cleaner tests
- More readable intent
- Reduced setup code

---

# Anonymous Requests

Authentication is optional.

If no active user is selected:

```java
bookService.getBooks();
```

The request executes without an Authorization header.

This enables:

- Public endpoint testing
- Unauthorized access testing
- Negative authorization scenarios

---

# Book Models

## BookQueryParams

Provides a builder-based query parameter construction mechanism.

Example:

```java
BookQueryParams queryParams =
        BookQueryParams.builder()
                .page(1)
                .limit(10)
                .author("Tolkien")
                .build();
```

---

## Advanced Filters

Supports dynamic operator-based filtering.

Example:

```java
BookQueryParams queryParams =
        BookQueryParams.builder()
                .filters(
                        Map.of(
                                "price[gte]", 10,
                                "price[lte]", 100
                        )
                )
                .build();
```

Supported operators:

- gt
- gte
- lt
- lte

---

## CreateBookRequest

DTO representing the payload for book creation.

Example:

```java
CreateBookRequest.builder()
        .title("Clean Code")
        .author("Robert Martin")
        .isbn("9780132350884")
        .price(29.99)
        .stock(50)
        .build();
```

---

# Services

## BookService

Currently supports:

### Get Books

```java
bookService.getBooks();
```

---

### Get Books With Filters

```java
bookService.getBooks(queryParams);
```

---

### Get Book By ID

```java
bookService.getBookById(bookId);
```

---

### Create Book

```java
bookService.createBook(bookRequest);
```

---

### Delete Book

```java
bookService.deleteBook(bookId);
```

---

# Assertions

Business logic verification is separated from test methods.

Example:

```java
BookAssertion.assertValueGreaterThanEqualsTo(
        prices,
        10
);
```

Benefits:

- Better reuse
- Cleaner tests
- Easier maintenance

---

# Example Test

```java
@Test
@UseUser(UserType.ADMIN)
public void shouldCreateBook() {

    CreateBookRequest request =
            CreateBookRequest.builder()
                    .title("Clean Code")
                    .author("Robert Martin")
                    .isbn("9780132350884")
                    .price(29.99)
                    .stock(50)
                    .build();

    bookService.createBook(request)
            .then()
            .statusCode(201);
}
```

---

# Authentication Flow

```text
@BeforeSuite
        │
        ▼
Login As Admin
        │
        ▼
Store In TokenManager
        │
        ▼
Login As Customer
        │
        ▼
Store In TokenManager
        │
        ▼
Tests Execute
        │
        ▼
@UseUser(...)
        │
        ▼
SessionManager
        │
        ▼
Authorized Requests
```

---

# Current Capabilities

✅ Config Management

✅ Endpoint Management

✅ AuthService

✅ Login & Register DTOs

✅ AuthToken DTO

✅ UserType Enum

✅ TokenManager

✅ Thread-Safe SessionManager

✅ @UseUser Annotation

✅ BookQueryParams Builder

✅ Dynamic Query Filters

✅ CreateBookRequest DTO

✅ BookService

✅ Assertion Layer

✅ Anonymous Request Support

---

# Roadmap

## Reporting

- Extent Reports
- TestNG Listeners
- Request/Response Logging
- Execution Dashboards

---

## Test Data

- Java Faker Integration
- Data Factory Pattern

---

## API Coverage

- Books
- Cart
- Orders
- Addresses
- Ratings

---

## Authentication Enhancements

- JWT Expiry Handling
- Automatic Token Refresh

---

## CI/CD

- Docker
- Jenkins
- GitHub Actions
- Azure DevOps

---

## Advanced Automation Topics

- Parallel Execution
- Schema Validation
- Performance Testing
- Workflow Automation
- End-to-End User Journeys

---