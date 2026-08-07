# Overview

Arkitekt provides a set of architectural patterns for building modern apps. The same core ideas apply whether you are targeting Android only or building a Kotlin Multiplatform project.

## Two paths, one approach

- **Android / Compose**: `ViewModel` + `ViewState` + Hilt DI
- **KMP / Decompose**: `BaseComponent` + State (data class) + Koin DI

Both paths share:

- Use cases for business logic (`UseCase`, `FlowUseCase` from `cr-usecases`)
- Stores (repositories) for data access and caching
- Events for one-shot UI communication

## Architecture layers

| Layer | Android / Compose | KMP / Decompose |
|-------|-------------------|-----------------|
| UI | `@Composable` | `@Composable` |
| Presentation | `ViewModel` | `BaseComponent` |
| Business Logic | `UseCase` / `FlowUseCase` | `UseCase` / `FlowUseCase` |
| Data | `Store` / `Dao` / `ApiService` | `Store` / `Dao` / `ApiService` |

## When to choose which path

- **Android-only projects**: use the Android path (`core` + `compose` modules with Hilt).
- **KMP projects** targeting Android and iOS: use the Decompose path (`decompose` module with Koin).

!!! note
    The KMP / Decompose path supports **Android and iOS** targets. Desktop and Web targets are not supported.

Both paths depend on the `cr-usecases` module, which is fully KMP-compatible. `UseCase` and `FlowUseCase` are interfaces, so business logic is shared across all targets regardless of the path you choose. Execution is driven through `CoroutineScopeOwner` via Kotlin context parameter extensions.

## Related pages

- [Android Architecture](android.md): components, state, events, navigation
- [KMP Architecture](kmp/components.md): components, state, events, navigation, factory generator
- [Stores](stores.md): the shared data layer pattern
