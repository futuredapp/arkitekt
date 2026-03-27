# Navigation — KMP

The KMP path uses the [Decompose](https://arkivanov.github.io/Decompose/) library for navigation. See the [official Decompose navigation docs](https://arkivanov.github.io/Decompose/navigation/overview/) for the full API reference.

For advanced patterns, see [Navigation — Advanced](navigation-advanced.md).

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
