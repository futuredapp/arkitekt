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
- **`CancellationException`:** calls `globalOnErrorLogger` only if the root cause is NOT a `CancellationException` (e.g., when using `getOrCancel`)

## `defaultErrorHandler`

Override `defaultErrorHandler` in your ViewModel or Component to customize error handling:

```kotlin
override fun defaultErrorHandler(exception: Throwable) {
    // custom handling instead of rethrowing
    viewState.error.value = exception.message
}
```

This is called by `launchWithHandler` after `globalOnErrorLogger` for non-cancellation exceptions.

## `Throwable.rootCause`

Extension property that traverses the `cause` chain to find the deepest (root) cause of an exception:

```kotlin
val root = exception.rootCause
```

This is used internally by `launchWithHandler` to determine whether a `CancellationException` was caused by an actual cancellation or by `getOrCancel`.
