# UseCase

`UseCase<ARGS, T>` is an interface for one-shot operations that return a single result.

## Defining a UseCase

Implement the `build` method with your business logic:

```kotlin
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) : UseCase<LoginData, User> {
    override suspend fun build(args: LoginData): User =
        authRepository.login(args.name, args.password)
}
```

For use cases without arguments, use `Unit` as the args type:

```kotlin
class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) : UseCase<Unit, User> {
    override suspend fun build(args: Unit): User =
        userRepository.getCurrentUser()
}
```

## Async Execution (Callbacks)

Execute with callback handlers for fire-and-forget style control flow. Requires the `-Xcontext-parameters` compiler flag and a `CoroutineScopeOwner` in scope (e.g. inside a ViewModel or Component):

```kotlin
loginUseCase.execute(LoginData(name, password)) {
    onStart { isLoading = true }
    onSuccess { result ->
        isLoading = false
        // handle result
    }
    onError { error ->
        isLoading = false
        // handle error
    }
    disposePrevious(true) // default: cancels previous execution
}
```

For `Unit` args, omit the args parameter:

```kotlin
refreshUseCase.execute {
    onSuccess { /* handle result */ }
}
```

### Callback Options

- **`onStart`** — called before the coroutine starts
- **`onSuccess(T)`** — called on successful completion with the result
- **`onError(Throwable)`** — called on error; also triggers `UseCaseErrorHandler.globalOnErrorLogger` (see [Error Handling](error-handling.md))
- **`disposePrevious`** — whether to cancel any previous execution before starting a new one (default `true`)

## Sync Execution (kotlin.Result)

Execute and get a `kotlin.Result<T>` back for sequential coroutine chains. This form is meant to be called from inside `launchWithHandler`:

```kotlin
fun onButtonClicked() = launchWithHandler {
    val user = loginUseCase
        .execute(LoginData(name, password))
        .getOrCancel { error -> showError(error) } // cancels the coroutine on failure
    // continue with user
}
```

- Returns `kotlin.Result<T>` — a success wrapping the value or a failure wrapping the exception
- Accepts an optional `cancelPrevious: Boolean` parameter (default `true`)
- `CancellationException` is rethrown directly; other exceptions are wrapped in `Result.failure`
- Use `getOrCancel { }`, `getOrThrow()`, `getOrDefault(...)`, `getOrNull()`, or `getOrElse { }` to unwrap

### getOrCancel

`getOrCancel` is a `kotlin.Result` extension from `cr-usecases`. On success it returns the value. On failure it calls the optional `doBeforeThrow` lambda (for side effects like showing an error) and then throws a `CancellationException`, cleanly cancelling the enclosing coroutine:

```kotlin
val data = loadUseCase.execute()
    .getOrCancel { showErrorMessage(it.message) }
```

`launchWithHandler` will route the cancellation through `UseCaseErrorHandler.globalOnErrorLogger` if the root cause is not itself a `CancellationException`.

## Fallback: Explicit Owner Overload

Projects that cannot enable `-Xcontext-parameters` can use the deprecated explicit-owner overload:

```kotlin
loginUseCase.execute(
    args = LoginData(name, password),
    coroutineScopeOwner = this,
) {
    onSuccess { /* ... */ }
}
```

This overload is marked `@Deprecated` and the IDE will suggest migrating to the context parameter form.
