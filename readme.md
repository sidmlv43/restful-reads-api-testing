# Restful Reads — REST Assured API Automation Framework Template

A Java + REST Assured + TestNG API automation framework built for the Restful Reads application. The framework includes role-based authentication, user-pool based session management, thread-safe parallel execution, JSON schema validation, data-driven testing with Java Faker, retry handling for flaky tests, request/response logging, Extent Reports, Maven profile-based execution, and Dockerized test execution.

I built this to actually learn how a senior SDET puts a framework together, not to write a pile of tests that hit endpoints and check status codes. I'm about a year into my career as an automation engineer, and this project is where I push past what my day job requires.

If you're looking for a REST Assured + TestNG framework template with role-based authentication, user isolation, parallel execution, reporting, Maven profiles, and Docker support already wired up, feel free to fork this and adapt it. The sections below explain not only what each piece does, but why it exists.

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

✅ Maven Profile-Based Execution

✅ Smoke / Critical / Regression Test Suites

✅ Dockerized Test Execution

✅ Environment-Specific Configuration

---

## Why this exists

Most tutorial-following frameworks I'd built before this had one big flaw: everything worked as long as you ran one test at a time.

The moment you tried running tests in parallel, or validating behavior across multiple user roles, the architecture started showing cracks.

This framework exists to solve those problems properly and to document the decisions made along the way.

As the framework evolved, another challenge appeared.

Running multiple user journeys in parallel meant several tests were competing for the same customer account.

Cart state, addresses, ratings, and future order data all became shared state.

That led to random failures, difficult debugging sessions, and tests influencing one another.

The introduction of a UserPool solved that problem and became one of the most important architectural decisions in the framework.

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
- Docker

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
│   ├── BookDataFactory
│   └── FileDataFactory
│
├── dataproviders
│
├── listeners
│   ├── UserContextListener
│   ├── ExtentTestListener
│   ├── RetryAnalyzer
│   └── RetryTransformer
│
├── testgroups
│   └── TestGroups
│
└── tests


suites
├── smoke.xml
├── critical.xml
└── regression.xml
```

---

## Role-based authentication, session management, and user pooling

This is currently the most important architectural piece of the framework.

Early versions of the framework authenticated a single Admin user and a single Customer user at suite startup and reused those tokens for every test.

That worked for basic CRUD testing.

It failed once user-specific journeys entered the picture:

- Cart workflows
- Checkout workflows
- Order history
- Address management
- Product ratings

Using a shared account meant every test was modifying the same user state.

To solve this, the framework introduced a UserPool.

At suite startup, the framework dynamically provisions user pools based on the configured thread count.

Rather than authenticating a fixed number of users every run, only the users required for execution plus a small buffer are authenticated.

Example:

```text
thread-count = 4

Admin Pool     = 6 Users
Customer Pool  = 6 Users
```

Internally:

```java
Map<UserType, BlockingQueue<User>>
```

is used to maintain role-specific pools.

Each authenticated user contains:

```java
email
password
token
userType
```

Tests declare required permissions using:

```java
@Test
@UseUser(UserType.CUSTOMER)
public void customerCanAddItemsToCart() {

}
```

Execution flow:

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

This provides dramatically better isolation for parallel execution.

---

## SessionManager

Session state is stored as:

```java
ThreadLocal<User>
```

rather than:

```java
ThreadLocal<String>
```

The currently executing thread has access to:

- Email
- Password
- JWT Token
- User Type

This became necessary once the framework evolved from token sharing to user leasing.

Without ThreadLocal, parallel execution would leak user sessions across threads.

---

## Service layer

`BaseService` centralizes everything every request needs:

- Base URI configuration
- Content-Type configuration
- Authorization header injection
- Request logging
- Response logging

Every service extends `BaseService`, ensuring common concerns are implemented once.

### Implemented

- AuthService
- BookService
- CartService

### In Progress

- AddressService
- RatingService
- UserService

---

## Request and response DTOs

Requests and responses are modeled separately.

Example request:

```java
CreateBookRequest.builder()
        .title("Clean Code")
        .author("Robert Martin")
        .price(29.99)
        .build();
```

Example response mapping:

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

Book filtering uses a builder pattern.

Example:

```java
BookQueryParams.builder()
        .author("Rick Riordan")
        .page(1)
        .limit(10)
        .build();
```

Advanced filtering:

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

## Test data generation

Java Faker powers all dynamic test data generation.

Example:

```java
CreateBookRequest request =
        BookDataFactory.createBook();
```

Multipart uploads are supported through:

```java
FileDataFactory
```

which provides reusable image test files for upload scenarios.

---

## Data-driven testing

Data Providers allow a single test implementation to execute against multiple datasets.

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

---

## JSON schema validation

Response schemas are maintained under:

```text
src/test/resources/schemas
```

Validation uses:

```java
matchesJsonSchemaInClasspath(...)
```

This helps detect:

- Missing fields
- Type changes
- Contract regressions
- Unexpected response structures

---

## Reporting with Extent Reports

Extent Reports generates:

```text
test-output/ExtentReport.html
```

Each report contains:

- Pass / Fail / Skip status
- Request logs
- Response logs
- Stack traces
- Categories
- Author metadata
- Zephyr references

A custom REST Assured filter automatically logs API traffic without requiring additional test code.

---

## Test metadata

Custom metadata annotations:

### Author

```java
@Author("Siddharth Malviya")
```

### Zephyr Test

```java
@ZephyrTest("BOOKS_101")
```

These currently provide reporting metadata while laying the groundwork for future test-management integrations.

---

## Retry handling

Retry execution is implemented globally through:

```java
RetryTransformer
```

and:

```java
RetryAnalyzer
```

The retry count is configurable:

```properties
retry_count=3
```

or:

```bash
mvn test -Dretry_count=5
```

Retries remain visible in Extent Reports to prevent flaky tests from silently disappearing behind a passing result.

---

## Parallel execution

Parallel execution is supported through TestNG.

Thread safety depends on:

```java
ThreadLocal<User>
```

and:

```java
ThreadLocal<ExtentTest>
```

Without both mechanisms, report entries and user sessions would become corrupted under parallel execution.

Parallelism can be configured through Maven:

```bash
mvn test -Pregression -Dthread-count=10
```

---

## Maven profiles and test suites

Three execution suites currently exist:

```text
suites/
├── smoke.xml
├── critical.xml
└── regression.xml
```

### Smoke

```bash
mvn test -Psmoke
```

### Critical

```bash
mvn test -Pcritical
```

### Regression

```bash
mvn test -Pregression
```

This allows CI pipelines to run lightweight validation suites separately from full regression coverage.

---

## Environment configuration

Environment-specific properties are supported.

Current environments:

```text
application-uat.properties
application-docker.properties
```

Examples:

### UAT

```bash
mvn test -Psmoke -Denv=uat
```

### Docker

```bash
mvn test -Psmoke -Denv=docker
```

The framework uses these property files to manage:

- Base URL
- Retries
- Timeouts
- Environment-specific configuration

---

## Docker support

The framework can be executed entirely inside Docker.

Benefits:

- Consistent execution environments
- Easier CI/CD integration
- Faster onboarding
- Elimination of local machine configuration differences

### Build the image

```bash
docker build -t restful-reads-tests .
```

### Execute default command

```bash
docker run --rm restful-reads-tests
```

Equivalent to:

```bash
mvn test -Psmoke,uat
```

---

### Execute regression suite

```bash
docker run --rm \
    -e SUITE=regression \
    -e ENVIRONMENT=docker \
    -e THREAD_COUNT=10 \
    restful-reads-tests
```

Equivalent to:

```bash
mvn test \
    -Pregression,docker \
    -Dthread-count=10
```

---

### Execute critical suite

```bash
docker run --rm \
    -e SUITE=critical \
    -e ENVIRONMENT=docker \
    -e THREAD_COUNT=4 \
    restful-reads-tests
```

Equivalent to:

```bash
mvn test \
    -Pcritical,docker \
    -Dthread-count=4
```

---

### Execute smoke suite

```bash
docker run --rm \
    -e SUITE=smoke \
    -e ENVIRONMENT=docker \
    restful-reads-tests
```

---

## Running against a dockerized backend

When running inside Docker, the framework communicates with the backend using Docker networking.

Example:

```properties
base.url=http://restful-reads-api:5000
```

instead of:

```properties
base.url=http://localhost:5000
```

because containers communicate through service names rather than localhost.

Example workflow:

### Start backend

```bash
docker compose up -d
```

Verify:

```bash
curl http://localhost:5000
```

### Execute framework

```bash
docker run --rm \
    --network restful-reads_default \
    -e SUITE=regression \
    -e ENVIRONMENT=docker \
    -e THREAD_COUNT=10 \
    restful-reads-tests
```

---

## Running the suite

### Smoke

```bash
mvn test -Psmoke
```

### Critical

```bash
mvn test -Pcritical
```

### Regression

```bash
mvn test -Pregression
```

### Custom thread count

```bash
mvn test -Pregression -Dthread-count=10
```

### Docker environment

```bash
mvn test -Pregression -Denv=docker
```

### Full example

```bash
mvn test \
    -Pregression,docker \
    -Dthread-count=10
```

---

## Using this as a template

The most reusable pieces are:

- BaseService
- UserPool
- SessionManager
- UserContextListener
- RetryTransformer
- RetryAnalyzer
- Extent Reporting
- Request/Response Logging
- DTO Architecture
- Maven Profile Structure

Book-specific services and models are simply domain examples that can be replaced with your own.

---

## What's not here yet

- GitHub Actions integration
- Jenkins pipeline integration
- Docker Compose orchestration for backend + automation
- Framework-managed test data cleanup
- MongoDB verification layer
- Contract testing enhancements
- Checkout journey automation
- Order journey automation
- Address journey automation

---

Still very much a work in progress.

I'm adding to this as I learn, and the gaps listed above are known weak spots I'm actively working through, not blind spots I've missed.