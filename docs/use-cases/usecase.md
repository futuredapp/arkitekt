# UseCase

`UseCase<ARGS, T>` is an abstract class for one-shot operations that return a single result.

## Defining a UseCase

Implement the `build` method with your business logic:

```kotlin
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) : UseCase<LoginData, User>() {
    override suspend fun build(args: LoginData): User =
        authRepository.login(args.name, args.password)
}
```

The `UseCase` class has a `deferred` property that allows tracking and cancellation of the current execution.

## Async Execution (Callbacks)

Execute with callback handlers for fine-grained control:

```kotlin
loginUseCase.execute(LoginData(name, password)) {
    onStart { isLoading.value = true }
    onSuccess { result ->
        isLoading.value = false
        // handle result
    }
    onError { error ->
        isLoading.value = false
        // handle error
    }
    disposePrevious(true) // default: cancels previous execution
}
```

### Callback Options

- **`onStart`** — called before the coroutine starts
- **`onSuccess`** — called on successful completion with the result
- **`onError`** — called on error. If not defined, the exception is thrown. If defined, `UseCaseErrorHandler.globalOnErrorLogger` is also called (see [Error Handling](error-handling.md))
- **`disposePrevious`** — whether to cancel any previous execution before starting a new one (default `true`)

For use cases with `Unit` arguments, you can omit the args parameter:

```kotlin
refreshUseCase.execute {
    onSuccess { /* handle result */ }
}
```

## Sync Execution (Result)

Execute and get a `Result<T>` back for sequential coroutine chains:

```kotlin
fun onButtonClicked() = launchWithHandler {
    val data = loginUseCase
        .execute(LoginData(name, password))
        .getOrDefault("Default")
    // continue with data
}
```

- Returns `Result<T>` (see [Result](result.md) for all available operations)
- Accepts a `cancelPrevious` parameter (default `true`)
