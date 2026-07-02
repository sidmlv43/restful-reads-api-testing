# Restful Reads API Automation Framework

A scalable API automation framework built using Java, TestNG, Rest Assured, and Lombok for testing the Restful Reads application.

---

## Features

- Rest Assured based API testing
- TestNG test execution
- Environment-aware configuration management
- Centralized endpoint management
- Authentication service abstraction
- Reusable request/response models
- Token caching and session management
- Thread-safe execution support
- Assertion layer for business validations
- Query parameter builder support
- Extent Reports integration (In Progress)
- Parallel execution ready

---

## Tech Stack

- Java 17
- TestNG
- Rest Assured
- Lombok
- Maven
- Jackson
- Extent Reports (Upcoming)

---

## Project Structure

```text
src
├── main
│   ├── config
│   ├── constants
│   ├── enums
│   ├── models
│   ├── services
│   ├── session
│   └── utils
│
├── test
│   ├── assertions
│   ├── base
│   ├── tests
│   └── reports
```

---

## Framework Architecture

```text
Tests
  │
  ▼
Assertions
  │
  ▼
Services
  │
  ▼
Endpoints
  │
  ▼
Rest Assured
  │
  ▼
REST API
```

---

## Authentication Flow

The framework authenticates predefined users once at suite startup and stores their tokens for reuse.

```text
@BeforeSuite
   │
   ├── Login as Admin
   ├── Login as Customer
   │
   ▼
TokenManager
   │
   ▼
Tests activate required user
   │
   ▼
SessionManager
```

---

## Session Management

### TokenManager

Stores JWT tokens for known user types.

```java
TokenManager.register(UserType.ADMIN, adminToken);
TokenManager.register(UserType.CUSTOMER, customerToken);
```

### SessionManager

Maintains the active user session for the current thread.

```java
SessionManager.use(UserType.ADMIN);
```

Services automatically pick up the active token:

```java
SessionManager.getToken();
```

---

## User Types

```java
public enum UserType {
    ADMIN,
    CUSTOMER
}
```

---

## Endpoint Management

Endpoints are maintained centrally.

Example:

```java
public class AuthEndpoints {

    public static final String LOGIN = "/api/auth/login";
    public static final String REGISTER = "/api/auth/register";

}
```

This avoids hardcoded URLs inside service classes.

---

## Services

### AuthService

Supports:

- Login
- Registration

Example:

```java
String token = authService.login(
        LoginRequest.builder()
                .email("admin@example.com")
                .password("adminpass")
                .build()
);
```

---

### BookService

Supports:

- Fetch books
- Filter books
- Pagination
- Sorting
- Advanced query parameters

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

## Query Parameter Builder

`BookQueryParams` provides a clean and reusable way to construct query parameters.

Example:

```java
BookQueryParams queryParams =
        BookQueryParams.builder()
                .page(1)
                .limit(10)
                .sort("-createdAt")
                .build();
```

---

### Advanced Filters

Supports operator-based filters.

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

## Assertions Layer

Business assertions are separated from tests.

Example:

```java
BookAssertion.assertValueGreaterThanEqualsTo(
        prices,
        10
);
```

Benefits:

- Reusability
- Cleaner test methods
- Better maintainability

---

## Example Test

```java
@Test
public void testPriceFilterWorks() {

    SessionManager.use(UserType.CUSTOMER);

    BookQueryParams queryParams =
            BookQueryParams.builder()
                    .filters(
                            Map.of(
                                    "price[gte]", 10
                            )
                    )
                    .build();

    Response response =
            bookService.getBooks(queryParams);

    List<Double> prices =
            response.jsonPath()
                    .getList(
                            "results.price",
                            Double.class
                    );

    BookAssertion.assertValueGreaterThanEqualsTo(
            prices,
            10
    );
}
```

---

## Configuration

Configuration values are managed via property files.

Example:

```properties
base.url=http://localhost:3000
timeout=5000
```

Accessed through:

```java
ConfigManager.getBaseUrl();
```

---

## Logging

Request and response logging is configured globally through Rest Assured filters.

Benefits:

- Centralized logging
- Consistent debugging experience
- No need for repetitive `.log().all()` statements

---

## Planned Enhancements

### Reporting

- Extent Reports
- Custom TestNG Listeners
- Request / Response Attachments
- Execution Summary Dashboard

### Authentication

- JWT Expiration Handling
- Token Auto Refresh
- Session Context Improvements

### API Coverage

- Books
- Cart
- Orders
- Ratings
- Addresses
- Authentication

### Advanced Rest Assured Topics

- JSON Schema Validation
- Data Providers
- Response Time Validation
- Workflow Testing
- Parallel Execution
- API Contract Validation

---

## Learning Objectives

This framework is being built with a strong focus on learning and applying:

- Rest Assured
- TestNG
- API Design
- Framework Design
- Test Architecture
- Thread Safety
- Reporting
- Authentication Strategies
- End-to-End API Workflows

---

## Current Status

### Implemented

- Configuration Management
- Endpoint Management
- AuthService
- Register & Login Models
- TokenManager
- SessionManager
- BookQueryParams
- Assertion Layer
- Global Logging

### In Progress

- Extent Reports
- TestNG Listeners

### Planned

- Cart Service
- Order Service
- Workflow Automation
- JWT Refresh Support

---