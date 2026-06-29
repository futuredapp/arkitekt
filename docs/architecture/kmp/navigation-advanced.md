# Advanced navigation

This page covers advanced navigation patterns for the KMP / Decompose path. For the basics, see [Navigation (KMP)](navigation.md).

## Consolidated navigation

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
    This pattern is not enforced by the library. For simple nav-hosts with one or two screens, inline anonymous objects (as shown in the [Parent component](navigation.md#parent-component) section) work fine. The consolidated approach pays off as the number of screens and cross-screen navigation grows.

## Passing results between screens

Use `ResultFlow<T>` to send a value from a child screen back to its parent. Results are routed by a
stable string key: the parent collects results for a key, the child sends a result for the same key,
and the navigation config carries only that key, never a live object.

Results survive configuration changes and process death. Each value is persisted into saved state
until it is collected once, so a result the child produced before the OS killed the process still
reaches the parent after it is recreated. This needs a one-time integration step: wiring the result
registry into your `AppComponentContext`, described in
[Components → Durable navigation results](components.md#durable-navigation-results).

!!! note
    Results must be `@Serializable`, one-shot, and small. They ride in the Android saved-state
    `Bundle` (~1 MB limit), so use them for ids and selections, not large payloads.

Declare keys as constants with `ResultKey<T>` so the type travels with the key and every key stays in
one place:

```kotlin
object HomeResultKeys {
    val Picker = ResultKey<String>("home.picker")
}

// Navigation config: carries only the key
@Serializable
data class PickerConfig(val resultKey: String)

// In the parent nav-host
private val pickerResult = resultFlow(HomeResultKeys.Picker)

init {
    lifecycle.doOnCreate {
        pickerResult
            .onEach { selected -> update(componentState) { copy(selection = selected) } }
            .launchIn(lifecycleScope)
    }
}

private fun openPicker() = stackNavigation.push(PickerConfig(HomeResultKeys.Picker.name))

// In the child (picker) component, which received resultKey from its config
fun onItemSelected(item: String) = launchWithHandler {
    resultFlow<String>(resultKey).sendResult(item) // suspending; call from a coroutine
    navigation.back()
}
```

### Key uniqueness

Keys are application-wide, so each parent must use a unique key; namespace them per destination
(`"home.picker"`, not `"picker"`). Reusing the same child component across two navigation branches is
safe: the child only echoes back whatever `resultKey` its config was given. If two parents use the
same key while both are on the stack, collecting the second throws an `IllegalStateException` right
away instead of silently crossing results between branches. Only one collector per key can be active
at a time. For multiple concurrent instances of the same screen, generate a per-instance key and store
it in the config (for example `"home.picker.$itemId"`).

## ResultFlow API reference

`ResultFlow<T>` extends `Flow<T>` and adds `suspend fun sendResult(item: T)` to send a value back from
a child destination to a parent. Obtain a durable instance from a component with the `resultFlow`
extension on the component context.

### Obtaining a ResultFlow

```kotlin
// Recommended: from a typed key constant
val pickerResult = resultFlow(HomeResultKeys.Picker)

// Or inline, inferring the serializer from the type
val pickerResult = resultFlow<String>("home.picker")
```

Sent values live in the application-wide `NavigationResultRegistry` (exposed on
`ArkitektComponentContext`), which persists undelivered results into the root component's
`StateKeeper`. Collect in `init { lifecycle.doOnCreate { ... } }`: every time the parent is recreated
it re-attaches its collector and replays any result that arrived while it was gone.
