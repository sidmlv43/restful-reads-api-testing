# Restful Reads — REST Assured API Automation Framework Template

A Java + REST Assured + TestNG API automation framework built for the Restful Reads application. The framework includes role-based authentication, user-pool based session management, thread-safe parallel execution, JSON schema validation, data-driven testing with Java Faker, retry handling for flaky tests, request/response logging, and Extent Reports for reporting.

I built this to actually learn how a senior SDET puts a framework together, not to write a pile of tests that hit endpoints and check status codes. I'm about a year into my career as an automation engineer, and this project is where I push past what my day job requires.

If you're looking for a REST Assured + TestNG framework template with role-based authentication, thread-safe session handling, user pooling, parallel execution support, and Extent reporting already wired up, feel free to fork this and adapt it. The sections below walk through how each piece works and why it's built that way, so you're not just copying code you don't understand.

---

## Current Framework Capabilities

✅ Role-Based Authentication

✅ UserPool-Based Session Management

✅ Thread-Safe Parallel Execution

✅ REST Assured Service Layer

✅ DTO Serialization & Deserialization

✅ Request/Response Logging

✅ Extent Reports

✅ Retry Analyzer

✅ Data Providers

✅ JSON Schema Validation

✅ Custom Test Metadata

✅ Java Faker Test Data Generation

✅ Query Parameter Builder

✅ Custom Assertion Layer

✅ User Isolation During Parallel Execution

---

## Why this exists

Most tutorial-following frameworks I'd built before this had one big flaw: everything worked as long as you ran one test at a time. The moment you tried running tests in parallel, or testing what happens when a customer, not an admin, hits a protected endpoint, the whole thing fell apart.

This project exists to fix that properly and to document each decision as I made it, including the ones I got wrong the first time.

As the framework grew, I discovered another problem. Running multiple user journeys in parallel meant different tests were competing for the same customer account. Cart state, addresses, and future order data all became shared state that could make tests influence one another.

That problem led to the introduction of a UserPool, which is currently one of the most important architectural pieces of the framework.

---

## Tech stack

- Java 17
- Maven
- TestNG
- REST Assured
- Jackson
- Lombok
- Extent Reports
- Java Faker
- JSON Schema Validator

---

## Project structure

```text
src/main/java/com/restfulReads
├── annotations
│   ├── @UseUser
│   ├── @Author
│   └── @ZephyrTest
│
├── config
│   ├── ConfigManager
│   └── RestAssuredConfig
│
├── constants
│
├── enums
│
├── models
│   ├── requests
│   └── responses
│
├── query
│   └── BookQueryParams
│
├── reporting
│
├── services
│   ├── BaseService
│   ├── AuthService
│   ├── BookService
│   ├── CartService
│   ├── AddressService
│   ├── RatingService
│   └── UserService
│
└── session
    ├── User
    ├── UserCredential
    ├── UserPool
    ├── UserPoolInitializer
    └── SessionManager


src/test/java/com/restfulReads
├── assertions
├── base
├── data
├── dataproviders
├── listeners
│   ├── UserContextListener
│   ├── ExtentTestListener
│   ├── RetryAnalyzer
│   └── RetryTransformer
│
└── tests
```

---

## Role-based authentication, session management, and user pooling

This is the part I spent the most time getting right, and it's the piece I'd point to first if you're using this project as a reference for your own framework.

Early versions of the framework authenticated a single Admin user and a single Customer user at suite startup. Those tokens were stored and reused throughout execution.

That worked perfectly fine for CRUD-style API tests.

It broke down once I started thinking about user journeys.

Consider:

- Cart workflows
- Checkout workflows
- Address management
- Order history
- Product ratings

Using a single shared customer account means every test is modifying the same user state.

One test adds items to the cart.

Another test expects an empty cart.

A third test creates an address.

Now you've got debugging sessions that feel more like archaeology than testing.

To solve this, I introduced a UserPool.

At suite startup, the framework logs in:

- 20 Admin users
- 20 Customer users

and stores them in separate pools.

Internally, the implementation uses:

```java
Map<UserType, BlockingQueue<User>>
```

Each authenticated user contains:

```java
email
password
token
userType
```

Tests still declare permissions using the same annotation:

```java
@Test
@UseUser(UserType.CUSTOMER)
public void customerCanAddItemsToCart() {

}
```

The execution flow now looks like this:

```text
@UseUser(CUSTOMER)
         │
         ▼
UserContextListener
         │
         ▼
UserPool.acquire(CUSTOMER)
         │
         ▼
SessionManager
         │
         ▼
API Requests
         │
         ▼
Test Complete
         │
         ▼
UserPool.release(user)
```

A user can only belong to one active test at a time.

This dramatically reduces the chance of parallel tests interfering with one another through:

- Cart state
- Addresses
- Orders
- Ratings
- User-specific resources

It also allows the framework to scale far beyond the original "one admin, one customer" design without changing test code.

---

## SessionManager

Session state is managed through:

```java
ThreadLocal<User>
```

instead of:

```java
ThreadLocal<String>
```

The active session now stores the entire authenticated user rather than only a JWT token.

That gives the framework access to:

- Email
- Password
- Token
- UserType

for the currently executing thread.

The switch became necessary once UserPool-based execution was introduced.

Without ThreadLocal, parallel execution would leak session data between worker threads.

This isn't defensive over-engineering anymore. It's load-bearing infrastructure.

---

## Service layer

`BaseService` centralizes everything every request needs:

- Base URI configuration
- Content-Type configuration
- Authorization header injection
- Request/response logging

Every service class extends it, which means those concerns only need to be implemented once.

The framework currently contains:

### Implemented

- AuthService
- BookService
- CartService

### In Progress

- AddressService
- RatingService
- UserService

The endpoints have already been mapped in constants. Some service implementations are still being built out.

Listed honestly here rather than implied as complete.

---

## Request and response DTOs

The framework uses dedicated DTOs for requests and responses.

Example request object:

```java
CreateBookRequest.builder()
        .title("Clean Code")
        .author("Robert Martin")
        .price(29.99)
        .build();
```

Requests are automatically serialized into JSON using Jackson.

Responses are mapped back into Java objects:

```java
Book book =
        response.as(Book.class);
```

Benefits:

- Type safety
- Cleaner assertions
- Better IDE support
- Easier maintenance

---

## Query builder support

Book search requests use a builder pattern.

Example:

```java
BookQueryParams params =
        BookQueryParams.builder()
                .author("Rick Riordan")
                .page(1)
                .limit(10)
                .build();
```

Advanced filtering is also supported:

```java
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

## Test data with Java Faker

Faker generates realistic book payloads through `BookDataFactory`, so tests aren't full of hardcoded titles, authors, and prices.

This is also what powers the data-driven tests via TestNG Data Providers.

Example:

```java
CreateBookRequest request =
        BookDataFactory.createBook();
```

Known gap:

There's still no framework-level cleanup mechanism for resources created during execution.

If a test creates a book, nothing currently guarantees it gets deleted.

My plan is to introduce a framework-managed test resource registry that tracks created entities and automatically cleans them up after execution.

That feels like a framework problem, not a test-writing problem.

---

## Data-driven testing

The framework supports TestNG Data Providers.

Example:

```java
@Test(
        dataProvider = "bookDataProvider",
        dataProviderClass =
                BookServiceDataProvider.class
)
public void testAdminCanCreateBook(
        CreateBookRequest request
) {

}
```

This allows a single test implementation to execute against multiple datasets without duplicating code.

---

## JSON schema validation

Responses are validated against schemas stored under:

```text
src/test/resources/schemas
```

using:

```java
matchesJsonSchemaInClasspath(...)
```

This catches API contract regressions separately from business assertions.

Examples:

- Missing fields
- Incorrect data types
- Unexpected response structures

---

## Reporting with Extent Reports

Extent Reports generates an interactive HTML report:

```text
test-output/ExtentReport.html
```

The framework captures:

- Pass / Fail / Skip status
- Stack traces
- Categories
- Author metadata
- Zephyr references
- Request logs
- Response logs

A custom REST Assured filter (`ExtentRestAssuredFilter`) automatically writes request and response information directly into the report.

So when a test fails, you can immediately see:

```text
HTTP Method
URI
Request Body
Response Status
Response Body
```

without rerunning the test.

---

## Test metadata

The framework supports custom metadata annotations:

### @Author

```java
@Test
@Author("Siddharth Malviya")
public void createBookTest() {

}
```

### @ZephyrTest

```java
@Test
@ZephyrTest("BOOKS_101")
public void createBookTest() {

}
```

These are currently used for reporting.

The longer-term goal is to make integration with tools like Zephyr, Jira, or Azure Test Plans easier if needed.

---

## Retry handling for flaky tests

Every test automatically receives retry support through:

```java
RetryAnalyzer
```

which is applied globally through:

```java
RetryTransformer
```

The current retry count is configurable through:

```properties
retry_count=3
```

or:

```bash
mvn test -Dretry_count=5
```

Retries appear directly in the Extent Report.

To validate the implementation, I intentionally broke passing tests and confirmed they retried the expected number of times before failing.

One thing I'm still evaluating:

Retries currently apply to every failure.

That's convenient, but it's not always useful.

A genuine assertion bug generally doesn't become less wrong on the third attempt.

I may eventually restrict retries to transient failures only.

---

## Parallel execution

The suite runs using:

```xml
parallel="classes"
```

with multiple worker threads.

Thread safety currently relies on:

```java
ThreadLocal<User>
```

for user sessions and:

```java
ThreadLocal<ExtentTest>
```

for reporting.

Without those two mechanisms, parallel execution would quickly start corrupting user state and report entries.

---

## Environment configuration

`ConfigManager` reads:

```bash
-Denv=uat
```

and loads:

```text
application-uat.properties
```

Configuration values such as:

- Base URL
- Timeout values
- Retry count

are environment-specific rather than hardcoded.

---

## Running the suite

```bash
mvn test -Denv=uat
```

If no environment is provided:

```text
uat
```

is used as the default.

---

## Using this as a template

If you're adapting this framework for your own API, the most reusable pieces are:

- BaseService
- UserPool
- SessionManager
- UserContextListener
- RetryTransformer
- RetryAnalyzer
- Extent Reporting
- Request/Response Logging
- DTO Architecture

The Book-specific pieces are simply example domain implementations that can be swapped out for your own services and models.

---

## What's not here yet

- Docker support
- Jenkins / CI integration
- Framework-managed test data provisioning
- Automated test-data cleanup
- Database verification layer
- Contract testing enhancements
- Checkout journey automation
- Order journey automation
- Address journey automation

---

Still very much a work in progress.

I'm adding to this as I learn, and the gaps listed above are known weak spots I'm actively working through, not blind spots I've missed.