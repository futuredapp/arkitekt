# Overview

Use cases encapsulate business logic and ensure it runs on background threads, keeping your UI layer clean and responsive.

## Types

Arkitekt provides two use case types in the `cr-usecases` module:

- **`UseCase<ARGS, T>`** — for one-shot operations that return a single result (e.g., API calls, database writes)
- **`FlowUseCase<ARGS, T>`** — for streaming operations that emit multiple values over time (e.g., observing database changes, real-time updates)

The `cr-usecases` module is **KMP-compatible** and works on both the Android and Decompose/KMP architecture paths.

## Execution

Use cases are executed through the `CoroutineScopeOwner` interface, which provides:

- **`coroutineScope`** — the scope in which use case coroutines are launched
- **`getWorkerDispatcher()`** — the dispatcher for background work (defaults to `Dispatchers.IO`)

`CoroutineScopeOwner` is implemented by:

- **`BaseViewModel`** (Android/Compose path) — automatically, using `viewModelScope`
- **`BaseComponent` subclasses** (Decompose/KMP path) — you implement `CoroutineScopeOwner` manually and set `coroutineScope = componentCoroutineScope`

This means you call `useCase.execute(...)` directly from your ViewModel or Component without worrying about dispatchers or scope management.
