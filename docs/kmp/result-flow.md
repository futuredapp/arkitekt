# Result Flow

`ResultFlow<T>` enables passing results back between navigation destinations in a Decompose/KMP architecture.

## Overview

`ResultFlow<T>` extends `Flow<T>` and adds the ability to send values back from a child destination to a parent.

- Backed by `MutableSharedFlow` internally
- Serializable -- recreated as an empty flow during deserialization, making it safe for use in navigation configurations
- Provides `suspend fun sendResult(item: T)` for emitting results

## Usage

Create a `ResultFlow` in the parent component, pass it to the child when navigating, and collect results:

```kotlin
// In parent component
val resultFlow = ResultFlow<String>()

// Pass to child when navigating
navigation.navigateToChild(resultFlow)

// Collect results from child
resultFlow.collect { result ->
    // handle result
}
```

In the child component, send a result back:

```kotlin
// In child component
resultFlow.sendResult("selected item")
```

## Factory Function

Create instances using the factory function:

```kotlin
val resultFlow = ResultFlow<String>()
```

## Serialization in Navigation Configs

Navigation configurations must be `@Serializable`. Use `ResultFlowSerializer` to annotate `ResultFlow` properties inside a config — it serializes as a no-op and recreates an empty flow on deserialization. The parent always holds the live instance, so the child never needs to reconstruct the flow.

```kotlin
@Serializable
data class PickerConfig(
    @Serializable(ResultFlowSerializer::class) val result: ResultFlow<String>,
)
```

## Collecting Results in a NavHost

Create the flow in the parent nav-host, start collecting immediately using `componentCoroutineScope`, then push the config:

```kotlin

private val pickerResults = ResultFlow<String>()

private fun openPicker() {
    val result = ResultFlow<String>()
    stackNavigation.push(PickerConfig(pickerResults))
}

init {
    lifecycle.doOnCreate {
        collectResults()
    }
}

private fun collectResults() = launchWithHandler {
    pickerResults.collectLatest { result ->
        // Process the result
    }
}
```
