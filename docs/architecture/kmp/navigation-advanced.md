# Navigation — Advanced

This page covers advanced navigation patterns for the KMP / Decompose path. For the basics, see [Navigation — KMP](navigation.md).

## Consolidated Navigation

For nav-hosts with many screens, we recommend consolidating all navigation logic into a dedicated class. This keeps the nav-host component decluttered and navigation logic easy to find.

The pattern works as follows:

1. Each screen component defines its navigation as an interface with **extension functions on itself** (e.g. `fun HomeComponent.navigateToDetail()`). This scopes each navigation call to the component that triggers it and allows duplicate function names like `navigateBack()` across different screens.
2. The nav-host defines an internal interface that extends all child screen navigation interfaces and holds the `stackNavigator`.
3. A single implementation class provides all navigation logic.

```kotlin
// Each screen defines its own navigation interface
interface HomeScreenNavigation : NavigationActions {
    fun HomeComponent.navigateToDetail()
}

interface DetailScreenNavigation : NavigationActions {
    fun DetailComponent.navigateBack()
}

// The nav-host's internal interface consolidates them all
internal interface HomeNavHostNavigation :
    HomeScreenNavigation,
    DetailScreenNavigation {
    val stackNavigator: StackNavigation<HomeDestination>
}

// Single implementation handles all navigation
internal class HomeNavHostNavigationImpl : HomeNavHostNavigation {
    override val stackNavigator = StackNavigation<HomeDestination>()

    override fun HomeComponent.navigateToDetail() =
        stackNavigator.pushNew(HomeDestination.Detail)

    override fun DetailComponent.navigateBack() =
        stackNavigator.pop()
}
```

The nav-host receives the navigation instance and passes it to child factories. Because the consolidated interface extends every screen's navigation interface, the same instance satisfies all children:

```kotlin
@GenerateFactory
internal class HomeNavHostComponent(
    @InjectedParam componentContext: AppComponentContext,
    @InjectedParam private val navigation: HomeNavHostNavigation,
) : AppComponent<Unit, Nothing>(componentContext, Unit) {

    val stack = childStack(
        source = navigation.stackNavigator,
        serializer = HomeDestination.serializer(),
        initialConfiguration = HomeDestination.Home,
        childFactory = { destination, ctx ->
            when (destination) {
                HomeDestination.Home -> HomeComponentFactory.createComponent(ctx, navigation)
                HomeDestination.Detail -> DetailComponentFactory.createComponent(ctx, navigation)
            }
        },
    ).asStateFlow()
}
```

!!! tip
    This pattern is not enforced by the library — for simple nav-hosts with one or two screens, inline anonymous objects (as shown in the [Parent Component](navigation.md#parent-component) section) work fine. The consolidated approach pays off as the number of screens and cross-screen navigation grows.

## Passing Results Between Screens

Use `ResultFlow<T>` to send a value from a child screen back to its parent. The parent creates the flow, passes it in the navigation config, and collects results. The child calls `sendResult()` when it has a value to return.

Because `ResultFlow` is a `Flow`, it must be declared `@Serializable` in the config using `ResultFlowSerializer`. On deserialization it is recreated as an empty flow — the parent always holds the live instance.

```kotlin
// Navigation config
@Serializable
data class PickerConfig(
    @Serializable(ResultFlowSerializer::class) val result: ResultFlow<String>,
)

// In the parent nav-host
private fun openPicker() {
    val result = ResultFlow<String>()
    result
        .onEach { selected -> update(componentState) { copy(selection = selected) } }
        .launchIn(lifecycleScope)
    stackNavigation.push(PickerConfig(result))
}

// In the child (picker) component
fun onItemSelected(item: String) {
    resultFlow.sendResult(item)   // suspending — call from a coroutine
    navigation.back()
}
```

## ResultFlow API Reference

`ResultFlow<T>` extends `Flow<T>` and adds the ability to send values back from a child destination to a parent.

- Backed by `MutableSharedFlow` internally
- Serializable — recreated as an empty flow during deserialization, making it safe for use in navigation configurations
- Provides `suspend fun sendResult(item: T)` for emitting results

### Creating a ResultFlow

```kotlin
val resultFlow = ResultFlow<String>()
```

### Serialization in Navigation Configs

Navigation configurations must be `@Serializable`. Use `ResultFlowSerializer` to annotate `ResultFlow` properties inside a config — it serializes as a no-op and recreates an empty flow on deserialization. The parent always holds the live instance, so the child never needs to reconstruct the flow.

```kotlin
@Serializable
data class PickerConfig(
    @Serializable(ResultFlowSerializer::class) val result: ResultFlow<String>,
)
```

### Collecting Results in a NavHost

Create the flow in the parent nav-host, start collecting immediately using `lifecycleScope`, then push the config:

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
