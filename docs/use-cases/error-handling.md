# Error Handling

Arkitekt provides several layers of error handling for use cases and coroutine execution.

## Global Error Logger

`UseCaseErrorHandler.globalOnErrorLogger` is a global callback for logging errors. Set it in your `Application.onCreate`:

```kotlin
UseCaseErrorHandler.globalOnErrorLogger = { error ->
    Logger.logError(error)
}
```

### When is `globalOnErrorLogger` called?

| Execution mode | Condition | Called? |
|---|---|---|
| Async `execute` | `onError` defined | Yes (error is logged AND passed to `onError`) |
| Async `execute` | `onError` not defined | No (exception is thrown/unhandled) |
| `launchWithHandler` | Non-`CancellationException` | Yes |
| `launchWithHandler` | `CancellationException` with non-cancellation root cause (e.g. via `getOrCancel`) | Yes |
| `launchWithHandler` | Pure `CancellationException` | No |

## `launchWithHandler`

`launchWithHandler` launches a coroutine with built-in try-catch error handling:

```kotlin
fun onButtonClicked() = launchWithHandler {
    val data = loadDataUseCase.execute(args).getOrThrow()
    // process data
}
```

Error handling behavior:

- **Non-`CancellationException`:** calls `globalOnErrorLogger`, then calls `defaultErrorHandler`
- **`CancellationException` caused by `getOrCancel`:** calls `globalOnErrorLogger` with the original exception
- **Pure `CancellationException`:** silently ignored

## `defaultErrorHandler`

`defaultErrorHandler` is called by `launchWithHandler` after `globalOnErrorLogger` for non-cancellation exceptions. By default, it rethrows the exception.

Override it in your ViewModel or Component to handle errors without rethrowing:

```kotlin
override fun defaultErrorHandler(exception: Throwable) {
    viewState.error = exception.message
}
```

## `getOrCancel`

`getOrCancel` is an extension on `kotlin.Result<VALUE>` (from `cr-usecases`). It is the recommended way to abort a coroutine cleanly when a sequential use case call fails:

```kotlin
fun onButtonClicked() = launchWithHandler {
    val user = loginUseCase
        .execute(LoginData(name, password))
        .getOrCancel { error -> showError(error.message) } // side-effect before cancel

    // only reached on success
    navigateToHome(user)
}
```

On success, `getOrCancel` returns the value. On failure, it:

1. Calls the optional `doBeforeThrow` lambda (skipped if the exception is already a `CancellationException`)
2. Throws a `CancellationException` whose `cause` is the original exception

The thrown `CancellationException` bubbles up through `launchWithHandler`, which logs the original cause via `globalOnErrorLogger` and then lets the coroutine cancel cleanly — `defaultErrorHandler` is **not** called.
