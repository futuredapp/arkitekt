# Result

`Result<VALUE>` is a sealed class returned by synchronous `UseCase.execute()` calls. It models success and failure without throwing exceptions.

## Variants

- **`Success<VALUE>(val value: VALUE)`** — holds the successful result
- **`Error(val error: Throwable)`** — holds the exception

## Destructuring

`Result` supports destructuring declarations:

```kotlin
val (value, error) = useCase.execute(args)
// value is non-null on success, error is non-null on failure
```

## CancellationException Handling

Most `Result` extension functions rethrow `CancellationException` to respect coroutine cancellation. This ensures that cancelling a coroutine scope still works correctly when using `Result` chains.

## Extension Functions

### Extracting Values

- **`getOrNull()`** — returns the value on success, `null` on error
- **`getOrDefault(defaultValue)`** — returns the value on success, `defaultValue` on error
- **`getOrElse { error -> fallback }`** — returns the value on success, computes a fallback from the error
- **`getOrThrow { error -> /* optional pre-throw action */ }`** — returns the value on success, throws the error otherwise
- **`getOrCancel { error -> /* optional action */ }`** — returns the value on success, throws `CancellationException` on error (cancels the coroutine safely)

### Transforming

- **`map { value -> newValue }`** — transforms the success value; exceptions are rethrown
- **`mapCatching { value -> newValue }`** — transforms the success value; exceptions are caught as `Error`

### Recovering

- **`recover { error -> fallbackValue }`** — recovers from an error with a fallback value
- **`recoverCatching { error -> fallbackValue }`** — recovers from an error; exceptions in the recovery block are caught

### Folding

- **`fold(onSuccess = { }, onError = { })`** — handles both success and error cases, returning a single value

## Utility Function

**`tryCatch { block }`** wraps a block in try-catch, returning `Success` on success or `Error` on exception:

```kotlin
val result = tryCatch { riskyOperation() }
```

## Chaining Example

```kotlin
fun loadData() = launchWithHandler {
    val data = getDataUseCase.execute()
        .map { transform(it) }
        .recover { fallbackData }
        .getOrThrow()

    val saved = saveUseCase.execute(data)
        .getOrCancel { showError(it) } // cancels coroutine on error
}
```
