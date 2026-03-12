# Utility Extensions

Arkitekt provides utility extensions for bridging Decompose and Kotlin Coroutines APIs, plus helpers for common patterns.

## Value / Flow Conversions

### `Value<T>.asStateFlow()`

Converts a Decompose `Value` to a Kotlin `StateFlow`:

```kotlin
val state: StateFlow<HomeState> = decomposeValue.asStateFlow()
```

### `Flow<T>.collectAsValue(initial, coroutineScope)`

Converts a Kotlin `Flow` to a Decompose `Value`:

```kotlin
val decomposeValue: Value<HomeState> = stateFlow.collectAsValue(
    initial = HomeState(),
    coroutineScope = componentCoroutineScope,
)
```

## State Management

### `update(mutableStateFlow) { transform }`

Mutates a `MutableStateFlow` using a transform function:

```kotlin
update(componentState) { copy(title = "New Title", isLoading = false) }
```

## Navigation

### `StackNavigator<C>.switchTab(configuration)`

Switches to a tab without recreating it. Unlike `bringToFront`, this does not recreate the configuration if its class is already on the stack -- it brings the existing instance to front instead:

```kotlin
navigation.switchTab(Config.Home)
```

This is useful for bottom navigation patterns where you want to preserve tab state when switching between tabs.
