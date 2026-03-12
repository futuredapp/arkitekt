# Overview

Arkitekt provides a set of architectural patterns for building modern apps. The same core ideas apply whether you are targeting Android only or building a Kotlin Multiplatform project.

## Two Paths, One Philosophy

- **Android / Compose** — `ViewModel` + `ViewState` + Hilt DI
- **KMP / Decompose** — `BaseComponent` + State (data class) + Koin DI

Both paths share:

- **Use Cases** for business logic (`UseCase`, `FlowUseCase` from `cr-usecases`)
- **Stores** (repositories) for data access and caching
- **Events** for one-shot UI communication

## Injection Hierarchy

| Layer | Android / Compose | KMP / Decompose |
|-------|-------------------|-----------------|
| UI | `@Composable` | `@Composable` |
| Presentation | `ViewModel` | `BaseComponent` |
| Business Logic | `UseCase` / `FlowUseCase` | `UseCase` / `FlowUseCase` |
| Data | `Store` / `Dao` / `ApiService` | `Store` / `Dao` / `ApiService` |

## When to Choose Which Path

- **Android-only projects** — use the Android path (`core` + `compose` modules with Hilt).
- **KMP projects** targeting iOS, Desktop, or Web — use the Decompose path (`decompose` module with Koin).

Both paths depend on the `cr-usecases` module, which is fully KMP-compatible. Business logic written with Use Cases can be shared across all targets regardless of the path you choose.
