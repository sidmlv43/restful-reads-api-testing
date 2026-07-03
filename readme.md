# Restful Reads API Automation Framework

A scalable API automation framework built using Java, TestNG, Rest Assured, Lombok, and Extent Reports for testing the Restful Reads application.

The primary focus of this framework is not only API automation but also learning and applying automation architecture, framework design, reporting, maintainability, scalability, and engineering practices expected from a Senior SDET.

---

# Tech Stack

- Java 17
- Maven
- TestNG
- Rest Assured
- Jackson
- Lombok
- Extent Reports
- Java Faker

---

# Project Structure

```text
src
├── main
│   └── java
│       └── com.restfullReads
│           ├── annotations
│           │   ├── Author
│           │   ├── UseUser
│           │   └── ZephyrTest
│           │
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
│           ├── assertions
│           ├── base
│           ├── data
│           ├── listeners
│           │   ├── UserContextListener
│           │   └── ExtentTestListener
│           │
│           ├── reporting
│           │   ├── ExtentManager
│           │   └── ExtentTestManager
│           │
│           └── tests
```

---

# Framework Architecture

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

# Configuration Management

Configuration values are managed centrally using `ConfigManager`.

Example:

```java
ConfigManager.getBaseUrl();
```

Benefits:

- Single source of truth
- Easy environment switching
- Better maintainability

---

# Endpoint Management

API endpoints are centralized into constant classes.

Example:

```java
AuthEndPoints.LOGIN
AuthEndPoints.REGISTER

BookEndpoints.BASE
```

Benefits:

- No hardcoded URLs
- Easier maintenance
- Better readability

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
                        .password("password")
                        .build()
        );
```

---

## Register Example

```java
String token =
        authService.register(
                RegisterRequest.builder()
                        .name("John Doe")
                        .email("john@example.com")
                        .password("password")
                        .build()
        );
```

---

# Authentication Models

## LoginRequest

```java
LoginRequest.builder()
        .email("user@example.com")
        .password("password")
        .build();
```

---

## RegisterRequest

```java
RegisterRequest.builder()
        .name("John Doe")
        .email("john@example.com")
        .password("password")
        .build();
```

---

## AuthToken

Represents JWT authentication responses.

Example:

```json
{
  "token": "<jwt-token>"
}
```

---

# Role-Based Authentication

## UserType

```java
public enum UserType {

    ADMIN,
    CUSTOMER

}
```

---

# TokenManager

Stores tokens associated with known user types.

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

Benefits:

- Login once per suite
- Faster execution
- Reduced API calls

---

# SessionManager

Maintains the active user token for the current thread.

Example:

```java
SessionManager.use(UserType.ADMIN);
```

Authentication headers are automatically added when a user session is active.

The implementation uses:

```java
ThreadLocal
```

to support future parallel execution.

---

# Authentication Flow

```text
@BeforeSuite
        │
        ▼
Authenticate Admin
        │
        ▼
TokenManager
        │
        ▼
Authenticate Customer
        │
        ▼
TokenManager
        │
        ▼
Execute Tests
        │
        ▼
@UseUser(...)
        │
        ▼
SessionManager
        │
        ▼
API Request
```

---

# User Context Annotation

## @UseUser

Specifies which user should execute a test.

Example:

```java
@Test
@UseUser(UserType.ADMIN)
public void createBookTest() {

}
```

Benefits:

- Declarative authentication
- Cleaner tests
- No manual token handling

---

# Anonymous Requests

Authentication is optional.

If no user context is specified:

```java
bookService.getBooks();
```

The request executes without an Authorization header.

This enables:

- Public endpoint testing
- Negative authorization testing
- Unauthorized user validation

---

# Book Models

## BookQueryParams

Builder-based query parameter construction.

Example:

```java
BookQueryParams queryParams =
        BookQueryParams.builder()
                .page(1)
                .limit(10)
                .author("Rick Riordan")
                .sort("-createdAt")
                .build();
```

---

## Advanced Filtering

Supports dynamic operator-based filters.

Example:

```java
BookQueryParams queryParams =
        BookQueryParams.builder()
                .filters(
                        Map.of(
                                "price[gte]", 10,
                                "price[lte]", 50
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

Represents book creation payload.

Example:

```java
CreateBookRequest.builder()
        .title("Clean Code")
        .author("Robert Martin")
        .genre("Programming")
        .price(29.99)
        .build();
```

---

## Book

Represents book response payload returned by the API.

Example:

```java
Book book =
        response.as(Book.class);
```

---

# Test Data Management

## BookDataFactory

Uses Java Faker to generate realistic test data.

Example:

```java
CreateBookRequest book =
        BookDataFactory.createBook();
```

Benefits:

- Reduces hardcoded test data
- Improves test reliability
- Better coverage

---

# Services

## BookService

Supports:

### Get Books

```java
bookService.getBooks();
```

---

### Get Books Using Filters

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

### Update Book

```java
bookService.updateBook(
        bookId,
        Map.of(
                "price",
                44.44
        )
);
```

---

### Delete Book

```java
bookService.deleteBook(bookId);
```

---

# Assertions Layer

Business assertions are separated from tests.

Example:

```java
BookAssertion.assertValueGreaterThanOrEqualsTo(
        prices,
        10
);
```

Benefits:

- Better reuse
- Cleaner tests
- Easier maintenance

---

# Reporting

## Extent Reports

The framework integrates Extent Reports to generate detailed HTML execution reports.

Generated Report:

```text
test-output/ExtentReport.html
```

The report captures:

- Pass/Fail/Skip Status
- Execution Time
- Stack Traces
- Categories
- Test Metadata
- Author Information
- Zephyr Test References

---

## Reporting Architecture

```text
Test Execution
        │
        ▼
ExtentTestListener
        │
        ▼
ExtentManager
        │
        ▼
ExtentTestManager
        │
        ▼
Extent Report
```

---

## ExtentManager

Maintains a singleton `ExtentReports` instance.

Responsible for:

- Reporter initialization
- Report configuration
- Report flushing

---

## ExtentTestManager

Provides thread-safe access to:

```java
ExtentTest
```

using:

```java
ThreadLocal<ExtentTest>
```

This ensures compatibility with future parallel execution.

---

# Test Metadata

The framework supports metadata annotations to improve traceability and reporting.

---

## @Author

Provides ownership information.

Example:

```java
@Test
@Author("Siddharth Malviya")
public void createBookTest() {

}
```

Appears in the report under:

```text
Author
```

---

## @ZephyrTest

Associates an automated test with an external test case.

Example:

```java
@Test
@ZephyrTest("RR-123")
public void createBookTest() {

}
```

Appears in the report under:

```text
Zephyr Test Case
```

This is designed for future integration with:

- Zephyr
- Jira
- Azure Test Plans

---

# Test Categorization

The framework uses native TestNG groups.

Example:

```java
@Test(
        groups = {
                "smoke",
                "critical",
                "books"
        }
)
```

Groups automatically appear as report categories.

Example:

```text
Smoke
Critical
Books
```

---

# Example Test

```java
@Test(
        description = "Admin can create a new book",
        groups = {
                "smoke",
                "critical",
                "books"
        }
)
@Author("Siddharth Malviya")
@ZephyrTest("RR-123")
@UseUser(UserType.ADMIN)
public void testAdminCanCreateBook() {

    CreateBookRequest request =
            BookDataFactory.createBook();

    bookService.createBook(request)
            .then()
            .statusCode(201);
}
```

---

# Current Capabilities

✅ Configuration Management

✅ Endpoint Management

✅ AuthService

✅ Login & Register DTOs

✅ AuthToken DTO

✅ UserType Enum

✅ TokenManager

✅ Thread-Safe SessionManager

✅ Role-Based Authentication

✅ @UseUser Annotation

✅ BookQueryParams Builder

✅ Dynamic Query Filtering

✅ CreateBookRequest DTO

✅ Book DTO

✅ BookService

✅ CRUD Operations

✅ Java Faker Support

✅ Assertion Layer

✅ Extent Reports

✅ @Author Annotation

✅ @ZephyrTest Annotation

✅ Test Categorization

✅ Anonymous Request Support

---

# Roadmap

## Reporting

✅ Extent Reports

✅ Author Metadata

✅ Zephyr Metadata

⬜ Request Logging

⬜ Response Logging

⬜ Environment Information

⬜ Custom Dashboard

⬜ Execution Metrics

---

## Authentication

⬜ JWT Expiration Detection

⬜ Automatic Token Refresh

⬜ Advanced Session Management

---

## API Coverage

⬜ Cart Service

⬜ Order Service

⬜ Rating Service

⬜ Address Service

---

## CI/CD

⬜ Dockerization

⬜ Jenkins Integration

⬜ GitHub Actions

⬜ Azure DevOps

---

## Advanced Automation Topics

⬜ Parallel Execution

⬜ JSON Schema Validation

⬜ Response Time Validation

⬜ Workflow Automation

⬜ End-to-End Business Flows

---

# Learning Objectives

This framework is being developed to strengthen expertise in:

- API Automation
- Rest Assured
- Framework Design
- Test Architecture
- Reporting
- Session Management
- Authentication Strategies
- TestNG Internals
- Custom Annotations
- Test Metadata
- CI/CD
- Docker
- Automation Engineering
- Senior SDET Practices