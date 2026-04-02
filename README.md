# http4k-todos

A no-nonsense TODO REST API built with Kotlin, [http4k](https://www.http4k.org/), [Exposed](https://github.com/JetBrains/Exposed), and compile-time dependency injection — no magic, no surprises.

## Design principles

### No annotations, no runtime crashes

Annotations drive too much behaviour at runtime — they're invisible contracts that only fail when it's too late. This project avoids annotation-driven frameworks entirely. If something is wrong, the compiler tells you, not a stack trace at startup (or worse, mid-request).

### Compile-time dependency injection

Dependencies are wired explicitly in plain Kotlin code. There is no DI container scanning the classpath, no `@Inject` / `@Component` / `@Bean` soup, and no possibility of a missing binding crashing the process at runtime. The object graph is constructed once, eagerly, and the compiler verifies it.

### Explicit transaction management

No `@Transactional` annotations, no Unit-of-Work magic hiding behind a proxy. Transactions are opened and committed explicitly, making it obvious at a glance what is and isn't part of a database transaction. This avoids the subtle bugs that come with Hibernate/Spring's implicit transaction propagation and lazy-loading surprises. Also I argue that statements inside the `transaction` block should be only affecting _one_ database. In that way we avoid lock contention on rows

If you do:
```sql
BEGIN;
UPDATE users SET balance = balance - 100 WHERE id = 1;
-- call external API (takes 2 seconds)
UPDATE users SET balance = balance + 100 WHERE id = 2;
COMMIT;
```

What happens:
- The first UPDATE: Locks row id = 1
- That lock is held: for the entire duration of the transaction including while your app is waiting on the API

### Type-safe routing, documentation, and implementation in one place

Routes are defined using the http4k contract DSL, which lets you describe your API in a single, type-safe declaration that simultaneously drives:

- **OpenAPI documentation** — generated directly from the route definitions, always in sync with the implementation.
- **Server implementation** — the same definition that documents the endpoint also wires up the handler, so there is no drift between spec and code.

No separate Swagger annotations, no code-gen step, no `@ApiOperation` sprinkled across controllers.

### No-nonsense configuration

This project uses Konf to keep configuration as explicit and type-safe as the rest of the codebase. Instead of string-based lookups or annotation-driven binding, config is defined as Kotlin properties, giving you compile-time safety, IDE support, and fast failure when something is misconfigured.
There is deliberately no reliance on external config formats like HOCON, JSON, or YAML. In many setups, those files are bundled into the JAR anyway, which makes them a poor fit for real runtime configuration. Instead, this project uses environment variables as the source of truth. Konf’s DSL ties default values directly to strongly-typed properties, so defaults live next to the code that uses them — not in a detached file — and remain fully type-safe.
Konf also composes configuration sources in a predictable, explicit way. Because configuration is just data, it integrates cleanly with the project’s plain Kotlin dependency injection: config objects are passed directly into constructors, keeping the dependency graph transparent and easy to test.

## Stack

| Layer                 | Library                                         |
|-----------------------|-------------------------------------------------|
| HTTP server & routing | [http4k](https://www.http4k.org/)               |
| Database access       | [Exposed](https://github.com/JetBrains/Exposed) |
| Dependency injection  | [Koin](https://insert-koin.io)                  |
| Configuration         | [Konf](https://github.com/uchuhimo/konf)                  |
| Language              | [Kotlin](https://kotlinlang.org/)               |

## Running

```bash
./gradlew run
```

OpenAPI docs are available at `/docs` once the server is running.
