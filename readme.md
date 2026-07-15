# REST Assured API Automation Framework Template

A production-style REST Assured Framework built using Java, TestNG, Extent Reports, Data Providers, JSON Schema Validation, Retry Analyzer, Request/Response Logging, and Parallel Execution.

This project demonstrates the architecture and engineering practices commonly used by Senior SDET and Test Automation Engineers when building scalable, maintainable, and enterprise-ready API automation frameworks.

The objective of this project is not only API testing, but also learning:

- Framework Design
- Test Architecture
- API Automation
- Reporting
- Test Data Management
- Parallel Execution
- Maintainable Automation Practices
- Senior SDET Engineering Concepts

---

# Features

✅ REST Assured Framework

✅ API Automation Framework

✅ TestNG Framework

✅ Extent Reports

✅ Request & Response Logging

✅ Data Providers

✅ JSON Schema Validation

✅ Retry Analyzer

✅ Parallel Execution

✅ Role-Based Authentication

✅ Custom Annotations

✅ DTO Serialization & Deserialization

✅ Query Parameter Builder

✅ Thread-Safe Session Management

✅ Java Faker Integration

---

# Who Is This Project For?

This repository can be used as:

- REST Assured Framework Template
- API Automation Framework Template
- Java TestNG Framework Reference Project
- SDET Learning Project
- API Testing Framework Example
- Automation Framework Starter Project
- Framework Design Reference
- REST API Automation Project

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
- JSON Schema Validator

---

# Covered Concepts

This project covers:

- REST Assured
- API Testing
- API Automation
- TestNG
- Data Providers
- JSON Schema Validation
- Extent Reports
- Retry Mechanism
- Parallel Execution
- DTO Serialization
- DTO Deserialization
- Request & Response Logging
- Framework Design
- Custom Annotations
- Authentication Management
- Session Management
- Java Automation Frameworks

---

# Project Structure

```text
src
├── main
│   └── java
│       └── com.restfulReads
│           ├── annotations
│           │   ├── Author
│           │   ├── UseUser
│           │   └── ZephyrTest
│           │
│           ├── config
│           │   ├── ConfigManager
│           │   └── RestAssuredConfig
│           │
│           ├── constants
│           ├── enums
│           │
│           ├── models
│           │   ├── requests
│           │   └── responses
│           │
│           ├── query
│           ├── session
│           │   ├── SessionManager
│           │   └── TokenManager
│           │
│           └── services
│               ├── base
│               │   └── BaseService
│               │
│               ├── AuthService
│               └── BookService
│
└── test
    └── java
        └── com.restfulReads
            ├── assertions
            ├── base
            ├── data
            ├── dataproviders
            ├── listeners
            ├── reporting
            └── tests

src/test/resources
├── schemas
└── testng.xml
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
BaseService
   │
   ▼
Rest Assured
   │
   ▼
REST API
```

---

# Authentication Architecture

The framework implements role-based authentication using cached JWT tokens.

```text
@BeforeSuite
        │
        ▼
Authenticate Users
        │
        ▼
TokenManager
        │
        ▼
Execute Test
        │
        ▼
@UseUser
        │
        ▼
SessionManager
        │
        ▼
Authenticated Request
```

Benefits:

- Login once per suite
- Faster execution
- Reduced API calls
- Cleaner test implementation

---

# User Context Annotation

## @UseUser

The framework allows authentication to be defined declaratively.

Example:

```java
@Test
@UseUser(UserType.ADMIN)
public void createBookTest() {

}
```

Benefits:

- No manual token handling
- Cleaner tests
- Better readability
- Reduced duplicate code

---

# Anonymous Requests

Authentication is completely optional.

Example:

```java
bookService.getBooks();
```

Useful for:

- Public endpoint validation
- Unauthorized access testing
- Negative test scenarios

---

# BaseService

All service classes inherit from `BaseService`.

Responsibilities:

- Base URI configuration
- Content-Type configuration
- Authorization management
- Extent request/response logging

Example:

```java
protected RequestSpecification request()
```

Benefits:

- Centralized configuration
- Easier maintenance
- Reduced duplication

---

# Request DTOs

Examples:

```java
LoginRequest
RegisterRequest
CreateBookRequest
```

Example:

```java
CreateBookRequest request =
        CreateBookRequest.builder()
                .title("Clean Code")
                .author("Robert Martin")
                .genre("Programming")
                .price(29.99)
                .build();
```

Request DTOs are automatically serialized into JSON.

---

# Response DTOs

Examples:

```java
AuthToken
Book
```

Example:

```java
Book createdBook =
        response.as(Book.class);
```

Benefits:

- Type Safety
- Better IDE Support
- Cleaner Assertions
- Easier Maintenance

---

# Query Builder

## BookQueryParams

Supports flexible and reusable query generation.

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

# Advanced Filtering

Supported Operators:

- gt
- gte
- lt
- lte

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

---

# Test Data Management

## BookDataFactory

The framework uses Java Faker for dynamic data generation.

Example:

```java
CreateBookRequest request =
        BookDataFactory.createBook();
```

Benefits:

- Dynamic datasets
- Reduced hardcoded values
- Improved coverage

---

# Data Driven Testing

The framework supports native TestNG Data Providers.

Example:

```java
@DataProvider(name = "bookDataProvider")
public Object[][] bookDataProvider() {

    Object[][] data =
            new Object[5][1];

    for (int i = 0; i < 5; i++) {
        data[i][0] =
                BookDataFactory.createBook();
    }

    return data;
}
```

Usage:

```java
@Test(
        dataProvider = "bookDataProvider",
        dataProviderClass =
                BookServiceTestDataProvider.class
)
public void testAdminCanCreateBook(
        CreateBookRequest request
) {

}
```

Benefits:

- Increased coverage
- Reduced duplication
- Better scalability

---

# Custom Assertion Layer

Business assertions are separated from test implementation.

Example:

```java
BookAssertion.assertValueGreaterThanOrEqualsTo(
        prices,
        10
);
```

Benefits:

- Reusability
- Cleaner tests
- Better maintainability

---

# Schema Validation

The framework validates API contracts using JSON Schemas.

Schemas are stored under:

```text
src/test/resources/schemas
```

Example:

```java
response.then()
        .body(
                matchesJsonSchemaInClasspath(
                        "schemas/book-schema.json"
                )
        );
```

Benefits:

- Contract Validation
- Response Structure Validation
- Breaking Change Detection

---

# Reporting

## Extent Reports

The framework integrates Extent Reports to produce interactive HTML reports.

Generated Report:

```text
test-output/ExtentReport.html
```

The report includes:

- Pass/Fail/Skip Status
- Execution Duration
- Author Information
- Zephyr References
- Categories
- Stack Traces
- Request & Response Logs

---

# Reporting Architecture

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

## Request & Response Logging

Every request and response is automatically published to Extent.

Captured Information:

- HTTP Method
- URI
- Headers
- Request Payload
- Response Status
- Response Payload

Example:

```text
Request
--------
POST /api/books

{
  ...
}

Response
---------
201

{
  ...
}
```

This significantly improves debugging and failure analysis.

---

# Retry Mechanism

The framework includes automatic retry support for transient failures.

Components:

```text
RetryAnalyzer
RetryTransformer
```

Benefits:

- Reduces flaky failures
- Better CI reliability
- Minimal test maintenance

---

# Test Metadata

## @Author

Provides ownership information.

Example:

```java
@Test
@Author("Siddharth Malviya")
public void createBookTest() {

}
```

Appears directly in Extent Reports.

---

## @ZephyrTest

Provides test management linkage.

Example:

```java
@Test
@ZephyrTest("BOOKS_101")
public void createBookTest() {

}
```

Appears directly in Extent Reports.

Future integrations:

- Zephyr
- Jira
- Azure Test Plans

---

# Test Categorization

Supports native TestNG Groups.

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

Categories automatically appear in Extent Reports.

---

# Parallel Execution

The framework supports parallel execution.

Thread safety is achieved using:

```java
ThreadLocal
```

within:

- SessionManager
- ExtentTestManager

Benefits:

- Improved execution speed
- Better scalability
- Safe parallel reporting

---

# Example Test

```java
@Test(
        description = "Admin can create a new book"
)
@Author("Siddharth Malviya")
@ZephyrTest("BOOKS_105")
@UseUser(UserType.ADMIN)
public void testAdminCanCreateBook(
        CreateBookRequest request
) {

    Book createdBook =
            bookService.createBook(request)
                    .then()
                    .statusCode(201)
                    .extract()
                    .as(Book.class);

    Assert.assertNotNull(
            createdBook.getId()
    );
}
```

---

# Current Capabilities

✅ REST Assured Framework

✅ API Automation Framework

✅ Java + TestNG Architecture

✅ Service Layer Architecture

✅ DTO Serialization

✅ DTO Deserialization

✅ Role-Based Authentication

✅ TokenManager

✅ SessionManager

✅ @UseUser Annotation

✅ Query Parameter Builder

✅ Dynamic Query Filtering

✅ Java Faker Integration

✅ Data Providers

✅ JSON Schema Validation

✅ Request & Response Logging

✅ Extent Reports

✅ @Author Annotation

✅ @ZephyrTest Annotation

✅ Test Categorization

✅ Retry Analyzer

✅ Retry Transformer

✅ Parallel Execution Support

✅ Anonymous Request Support

✅ Assertion Layer

---

# Roadmap

## CI/CD

⬜ Docker Support

⬜ Jenkins Integration

⬜ GitHub Actions

⬜ Azure DevOps Pipelines

---

## Quality & Validation

⬜ Checkstyle Integration

⬜ Database Validation

⬜ Extended Contract Testing

---

## Framework Enhancements

⬜ Environment Profiles

⬜ Excel-Based Data Providers

⬜ Centralized Test Data Repository

⬜ Custom Retry Configuration

---

# Keywords

REST Assured Framework

REST Assured Framework Template

API Automation Framework

Java API Automation Framework

REST Assured Project

REST Assured GitHub Project

TestNG Framework

SDET Framework

Automation Testing Framework

API Testing Framework

Java Test Automation

REST API Testing

Automation Framework Template

Senior SDET Project

Java TestNG Automation Framework

---

# Learning Objectives

This project is being developed to strengthen expertise in:

- API Automation
- Rest Assured
- Framework Design
- TestNG Internals
- Reporting
- Session Management
- Data Driven Testing
- Parallel Execution
- Contract Validation
- Custom Annotations
- CI/CD
- Docker
- Jenkins
- Senior SDET Practices