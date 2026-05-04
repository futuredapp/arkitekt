# FlowUseCase

`FlowUseCase<ARGS, T>` is an interface for streaming operations that emit multiple values over time.

## Defining a FlowUseCase

Implement the `build` method returning a `Flow<T>`:

```kotlin
class ObserveFormUseCase @Inject constructor(
    private val formStore: FormStore,
) : FlowUseCase<Unit, Pair<String, String>> {
    override fun build(args: Unit): Flow<Pair<String, String>> =
        formStore.getFormFlow()
}
```

## Execution

Execute with callback handlers to react to each emitted value. Requires the `-Xcontext-parameters` compiler flag and a `CoroutineScopeOwner` in scope:

```kotlin
observeUserUseCase.execute(Unit) {
    onStart { /* called before flow starts */ }
    onNext { value -> /* called for each emitted value */ }
    onError { error -> /* called on error */ }
    onComplete { /* called when flow completes without error */ }
    disposePrevious(true) // default
}
```

For `Unit` args, omit the args parameter:

```kotlin
observeUserUseCase.execute {
    onNext { value -> /* ... */ }
}
```

### Callback Options

- **`onStart`** — called before the flow collection begins
- **`onNext(T)`** — called for each value emitted by the flow
- **`onError(Throwable)`** — called when the flow throws an error; also triggers `UseCaseErrorHandler.globalOnErrorLogger`
- **`onComplete`** — called when the flow completes successfully (without error)
- **`disposePrevious`** — whether to cancel any previous flow collection before starting a new one (default `true`)

The running `Job` is stored in `CoroutineScopeOwner.useCaseJobPool` keyed by the use case instance. It is cancelled automatically when `disposePrevious` is `true` or when `useCaseScope` is cancelled.

## Transforming Emitted Values

To transform values before `onNext`, apply Flow operators inside `build`:

```kotlin
class ObserveUserNamesUseCase @Inject constructor(
    private val userStore: UserStore,
) : FlowUseCase<Unit, String> {
    override fun build(args: Unit): Flow<String> =
        userStore.observeUsers().map { it.fullName }
}
```

## Common Patterns

`FlowUseCase` is commonly used for:

- Observing data stores or repositories
- Watching database changes (e.g., Room or SQLDelight flows)
- Listening to real-time updates (e.g., WebSocket streams)
- Monitoring connectivity or sensor data
