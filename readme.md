# Restful Reads — Rest Assured API Automation Framework

A Java + Rest Assured + TestNG API automation framework built for the Restful Reads app. It covers role-based JWT authentication, thread-safe parallel execution, JSON schema validation, data-driven testing with Java Faker, retry handling for flaky tests, and Extent Reports for reporting.

I built this to actually learn how a senior SDET puts a framework together — not to write a pile of tests that hit endpoints and check status codes. I'm about a year into my career as an automation engineer, and this project is where I push past what my day job requires.

If you're looking for a Rest Assured + TestNG framework template with role-based auth, thread-safe session handling, and Extent reporting already wired up, feel free to fork this and adapt it — the sections below walk through how each piece works and why it's built that way, so you're not just copying code you don't understand.

## Why this exists

Most tutorial-following frameworks I'd built before this had one big flaw: everything worked as long as you ran one test at a time. The moment you tried running tests in parallel, or testing what happens when a customer — not an admin — hits a protected endpoint, the whole thing fell apart. This project exists to fix that properly, and to document each decision as I made it, including the ones I got wrong the first time.

## Tech stack

Java 17, Maven, TestNG, Rest Assured, Lombok, Jackson, Extent Reports, Java Faker.

## Project structure

```
src/main/java/com/restfulReads
├── annotations     → @UseUser, @Author, @ZephyrTest
├── config          → environment config + Rest Assured setup
├── constants       → endpoint URLs, kept out of the service classes
├── models          → request/response DTOs
├── query           → BookQueryParams builder
├── services        → AuthService, BookService, BaseService
├── session         → TokenManager, SessionManager
└── reporting       → Extent Reports integration

src/test/java/com/restfulReads
├── assertions      → reusable business assertions
├── base            → BaseTest, suite-level setup
├── data            → BookDataFactory (Faker-based)
├── dataproviders
├── listeners       → UserContextListener, ExtentTestListener, RetryAnalyzer, RetryTransformer
└── tests
```

## Role-based authentication and session management

This is the part I spent the most time getting right, and it's the piece I'd point to first if you're using this as a reference for your own framework.

At suite startup, the framework logs in as an admin and a customer once, and caches both JWT tokens through `TokenManager`. Individual tests declare which user they need with a custom annotation instead of calling login code manually:

```java
@Test
@UseUser(UserType.ADMIN)
public void adminCanCreateBook() { ... }
```

A TestNG `IInvokedMethodListener` (`UserContextListener`) reads that annotation before the test method runs, sets the active user for that thread via `SessionManager`, and clears it afterward — pass, fail, or skip, doesn't matter. Getting that cleanup step right took a couple of iterations. My first pass didn't clear sessions reliably on failure, which meant a test could silently inherit the wrong user's token if it ran on a thread pool worker that had just handled a different test. Not a fun bug to chase down, glad I caught it before it caused a confusing false pass somewhere.

If a test doesn't use `@UseUser` at all, requests go out with no Authorization header. That's intentional — it's how public-endpoint tests and unauthorized-access tests work without a separate code path or a fake "guest" user.

Session state lives in a `ThreadLocal<String>`, which matters because the suite genuinely runs in parallel (`parallel="classes"`, 10 threads in `testng.xml`) — this isn't defensive over-engineering, it's load-bearing. Take the `ThreadLocal` out and parallel runs will leak tokens across threads.

## Service layer

`BaseService` centralizes everything every request needs: base URI, content type, attaching the auth header when one exists, and logging the request/response into the Extent report through a custom Rest Assured filter. Every service class extends it, so none of that has to be repeated.

Currently implemented: `AuthService` (login, register) and `BookService` (get, create, update, delete, and query filtering with operators like `price[gte]` and `price[lte]`). `CartService`, `AddressService`, `RatingService`, and `UserService` exist as stubs — endpoints are mapped in constants, the service methods aren't written yet. Listed honestly here rather than implied as finished.

## Test data with Java Faker

Faker generates realistic book payloads through `BookDataFactory`, so tests aren't full of hardcoded titles, authors, and prices. This is also what powers the data-driven tests via TestNG's `@DataProvider`.

Known gap: there's no framework-level cleanup mechanism yet for data created during a run. If a test creates a book, nothing guarantees it gets deleted afterward — some of my current dependent test chains rely on a later test doing the deleting, which is fragile. A proper fix looks like a per-test registry of created resource IDs, torn down in `@AfterMethod` regardless of outcome. That's the next real infrastructure piece, not a test-writing fix.

## JSON schema validation

Responses are validated against JSON schemas stored under `src/test/resources/schemas`, using `rest-assured`'s `matchesJsonSchemaInClasspath`. This catches breaking API contract changes — a field renamed or a type changed — separately from functional assertions.

## Reporting with Extent Reports

Extent Reports generates an interactive HTML report (`test-output/ExtentReport.html`) after every run. A custom Rest Assured filter (`ExtentRestAssuredFilter`) logs the HTTP method, URI, and request/response bodies into the report for every call, so when a test fails, you can see exactly what went over the wire without re-running anything.

`@Author` and `@ZephyrTest` are custom annotations that show up as metadata in the report. I added them mostly to practice thinking about test traceability the way real test-management tooling expects — this isn't wired up to an actual Zephyr or Jira instance.

Reporting is also thread-safe, using a second `ThreadLocal` (`ExtentTestManager`) so parallel test execution doesn't cross-contaminate report entries between threads.

## Retry handling for flaky tests

Every test gets a retry analyzer applied globally through an `IAnnotationTransformer` (`RetryTransformer`, which sets `RetryAnalyzer` on every `@Test` method). `retry_count` is configurable per environment — defaults to 3 in `application-uat.properties`, overridable with `-Dretry_count`. Retries show up as warnings in the Extent report, so a retried test stays visible instead of disappearing behind a green checkmark.

To actually confirm this worked rather than assuming it did, I temporarily broke a passing assertion on purpose — changed an expected status code so the test would fail every time — and watched it retry 3 times and then fail in the report as expected, before reverting it. I'd rather catch "my retry logic doesn't actually retry" this way than find out during a real flaky run in CI.

Still deciding on one thing: retry currently applies to every test, not just ones prone to genuine transient failure (timeouts, connection resets). A test failing because of a real assertion bug also retries 3 times before reporting red, which just delays the signal rather than helping. Scoping retry to specific exception types is on the list.

## Environment configuration

`ConfigManager` reads a `-Denv` system property (defaults to `uat`) and loads the matching `application-{env}.properties` file — base URL, timeout, and retry count are all environment-scoped this way rather than hardcoded.

## Running the suite

```
mvn test -Denv=uat
```

Omit `-Denv` and it falls back to `uat`.

## Using this as a template

If you're adapting this for your own API: the pieces that transfer directly regardless of what you're testing are `BaseService`, `SessionManager`/`TokenManager`, `UserContextListener`, and the retry/reporting listeners — none of them know anything about books specifically. The Book-specific parts (`BookService`, `BookQueryParams`, `BookDataFactory`, schemas) are the part you'd swap out for your own domain.

## What's not here yet

- CI/CD (GitHub Actions or Jenkins)
- Cart, Order, Address, Rating services — endpoints are mapped, services aren't written
- A real test-data cleanup mechanism
- Better isolation between a couple of book tests that currently share state through an instance field — works today, but it's more fragile than I'd like, and it's next on my list to fix properly

---

Still very much a work in progress — I'm adding to this as I learn, and the gaps listed above are known weak spots I'm actively working through, not blind spots I've missed.