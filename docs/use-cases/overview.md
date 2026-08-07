# Overview

Use cases encapsulate business logic and ensure it runs on background threads, keeping your UI layer clean and responsive.

## Types

Arkitekt provides two use case types in the `cr-usecases` module:

- **`UseCase<ARGS, T>`**: for one-shot operations that return a single result (e.g. API calls, database writes)
- **`FlowUseCase<ARGS, T>`**: for streaming operations that emit multiple values over time (e.g. observing database changes, real-time updates)

Both are **interfaces**. The `cr-usecases` module is **KMP-compatible** and works on both the Android and Decompose/KMP architecture paths.

## Execution

Use cases are executed through the `CoroutineScopeOwner` interface. Its key members are:

- **`useCaseScope`**: the `CoroutineScope` in which use case coroutines are launched
- **`useCaseJobPool`**: a `MutableMap<Any, Job>` that tracks running jobs for cancellation
- **`getWorkerDispatcher()`**: the dispatcher for background work (defaults to `Dispatchers.IO`)
- **`launchWithHandler { }`**: launches a coroutine with structured error handling

`CoroutineScopeOwner` is implemented by:

- **`BaseViewModel`** (Android/Compose path): automatically, using `viewModelScope`
- **`BaseComponent` subclasses** (Decompose/KMP path): implement `CoroutineScopeOwner` manually and set `useCaseScope = lifecycleScope`

## Calling execute

The `execute(...)` extensions on `UseCase` and `FlowUseCase` use Kotlin context parameters. They are called directly from inside any `CoroutineScopeOwner`:

```kotlin
// inside a ViewModel or Component that implements CoroutineScopeOwner
loginUseCase.execute(args) {
    onStart { /* ... */ }
    onSuccess { result -> /* ... */ }
    onError { error -> /* ... */ }
}
```

The `CoroutineScopeOwner` context is resolved automatically by the compiler, so no explicit receiver is needed. See [UseCase](usecase.md) and [FlowUseCase](flow-usecase.md) for full details.
