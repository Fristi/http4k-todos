# http4k-todos

A no-nonsense TODO REST API built with Kotlin, [http4k](https://www.http4k.org/), [Exposed](https://github.com/JetBrains/Exposed), and compile-time dependency injection — no magic, no surprises.

## Design principles

### No annotations, no runtime crashes

Annotations drive too much behaviour at runtime — they're invisible contracts that only fail when it's too late. This project avoids annotation-driven frameworks entirely. If something is wrong, the compiler tells you, not a stack trace at startup (or worse, mid-request).

### Compile-time dependency injection

Dependencies are wired explicitly in plain Kotlin code. There is no DI container scanning the classpath, no `@Inject` / `@Component` / `@Bean` soup, and no possibility of a missing binding crashing the process at runtime. The object graph is constructed once, eagerly, and the compiler verifies it.

### Explicit transaction management

No `@Transactional` annotations, no Unit-of-Work magic hiding behind a proxy. Transactions are opened and committed explicitly, making it obvious at a glance what is and isn't part of a database transaction. This avoids the subtle bugs that come with Hibernate/Spring's implicit transaction propagation and lazy-loading surprises.

### Type-safe routing, documentation, and implementation in one place

Routes are defined using the http4k contract DSL, which lets you describe your API in a single, type-safe declaration that simultaneously drives:

- **OpenAPI documentation** — generated directly from the route definitions, always in sync with the implementation.
- **Server implementation** — the same definition that documents the endpoint also wires up the handler, so there is no drift between spec and code.

No separate Swagger annotations, no code-gen step, no `@ApiOperation` sprinkled across controllers.

## Stack

| Layer | Library |
|---|---|
| HTTP server & routing | [http4k](https://www.http4k.org/) |
| Database access | [Exposed](https://github.com/JetBrains/Exposed) |
| Dependency injection | Manual / compile-time |
| Language | [Kotlin](https://kotlinlang.org/) |

## Running

```bash
./gradlew run
```

OpenAPI docs are available at `/docs` once the server is running.
