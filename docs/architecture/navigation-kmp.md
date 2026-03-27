# Navigation — KMP

The KMP path uses the [Decompose](https://arkivanov.github.io/Decompose/) library for navigation. See the [official Decompose navigation docs](https://arkivanov.github.io/Decompose/navigation/overview/) for the full API reference.

## NavigationActions

`NavigationActions` is a marker interface for defining navigation contracts:

```kotlin
interface HomeNavigation : NavigationActions {
    fun toDetail()
    fun toSettings()
}
```

## NavigationActionsProducer

`NavigationActionsProducer<NAV>` is an interface for components that expose a navigation contract:

```kotlin
class HomeComponent(
    componentContext: AppComponentContext,
    private val navigation: HomeNavigation,
) : BaseComponent<HomeState, HomeUiEvent>(componentContext, HomeState()),
    NavigationActionsProducer<HomeNavigation> {

    override val navigation = navigation

    fun onDetailClick() {
        navigation.toDetail()
    }
}
```

## Parent Component

Navigation is typically implemented in the parent component that manages the child stack. The parent creates a `StackNavigation`, defines configurations, and implements the `NavigationActions` interface for each child:

```kotlin
class RootComponent(
    componentContext: AppComponentContext,
) : BaseComponent<Unit, Nothing>(componentContext, Unit) {

    private val stackNavigation = StackNavigation<Config>()

    val childStack = childStack(
        source = stackNavigation,
        initialConfiguration = Config.Home,
        childFactory = ::createChild,
    )

    private fun createChild(config: Config, componentContext: ComponentContext) = when (config) {
        Config.Home -> HomeComponent(
            componentContext = componentContext,
            navigation = object : HomeNavigation {
                override fun toDetail() { stackNavigation.push(Config.Detail) }
                override fun toSettings() { stackNavigation.push(Config.Settings) }
            },
        )
        // ...
    }
}
```

## switchTab

`switchTab` is an extension on `StackNavigator<C>` designed for bottom navigation. It works like `bringToFront` but does **not** recreate the configuration if its class is already on the stack — it brings the existing instance to the front, preserving its state.

```kotlin
// In a nav-host managing bottom navigation tabs
fun onTabSelected(tab: Tab) {
    val config = when (tab) {
        Tab.Home -> Config.Home
        Tab.Profile -> Config.Profile
    }
    stackNavigation.switchTab(config)
}
```

## NavHost Pattern

For apps with multiple sections (tabs, auth states), use a hierarchy of nav-host components. Each nav-host can own any combination of `StackNavigation` and `SlotNavigation` instances — for example a stack for the main flow and one or more slots for dialogs or bottom sheets. It implements the `NavigationActions` interfaces for the children it creates.

Screen components define a `NavigationActions` interface but never implement it — the parent nav-host provides the implementation as an anonymous object in `createChild`. This keeps screen components fully decoupled from the navigation structure above them.

A typical 3-level hierarchy looks like this:

```
RootNavHostComponent       — SlotNavigation: Login ↔ SignedIn
  SignedInNavHostComponent — StackNavigation: Home tab / Profile tab
    HomeNavHostComponent   — StackNavigation: First → Second → Third
```

```kotlin
// Top-level: switches between login and signed-in using a slot
class RootNavHostComponent(
    ctx: AppComponentContext,
) : BaseComponent<Unit, Nothing>(ctx, Unit) {

    private val navigation = SlotNavigation<RootConfig>()

    val child = childSlot(
        source = navigation,
        serializer = RootConfig.serializer(),
        initialConfiguration = { RootConfig.Login },
        childFactory = ::createChild,
    )

    private fun createChild(config: RootConfig, ctx: ComponentContext) = when (config) {
        RootConfig.Login -> LoginComponent(
            componentContext = ctx.toAppContext(),
            navigation = object : LoginNavigation {
                override fun onLoginSuccess() = navigation.activate(RootConfig.SignedIn)
            },
        )
        RootConfig.SignedIn -> SignedInNavHostComponent(ctx.toAppContext())
    }
}

// Second level: manages top-level tab stack
class SignedInNavHostComponent(
    ctx: AppComponentContext,
) : BaseComponent<Unit, Nothing>(ctx, Unit) {

    private val stackNavigation = StackNavigation<SignedInConfig>()

    val childStack = childStack(
        source = stackNavigation,
        serializer = SignedInConfig.serializer(),
        initialConfiguration = SignedInConfig.Home,
        childFactory = ::createChild,
    )

    private fun createChild(config: SignedInConfig, ctx: ComponentContext) = when (config) {
        SignedInConfig.Home -> HomeNavHostComponent(
            componentContext = ctx.toAppContext(),
            navigation = object : HomeNavHostNavigation {
                override fun toProfile() = stackNavigation.push(SignedInConfig.Profile)
            },
        )
        SignedInConfig.Profile -> ProfileComponent(
            componentContext = ctx.toAppContext(),
            navigation = object : ProfileNavigation {
                override fun back() = stackNavigation.pop()
            },
        )
    }
}
```

!!! note "Navigation configs must be serializable"
    All navigation configurations must be `@Serializable` data classes or objects. Decompose uses serialization to preserve the full navigation stack across process death and configuration changes.

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
    This pattern is not enforced by the library — for simple nav-hosts with one or two screens, inline anonymous objects (as shown in the Parent Component section) work fine. The consolidated approach pays off as the number of screens and cross-screen navigation grows.

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
        .launchIn(componentCoroutineScope)
    stackNavigation.push(PickerConfig(result))
}

// In the child (picker) component
fun onItemSelected(item: String) {
    resultFlow.sendResult(item)   // suspending — call from a coroutine
    navigation.back()
}
```

See [Result Flow](../kmp/result-flow.md) for the full API reference.
