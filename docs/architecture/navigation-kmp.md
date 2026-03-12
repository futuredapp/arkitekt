# Navigation — KMP

The KMP path uses the [Decompose](https://github.com/arkivanov/Decompose) library for navigation.

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

`switchTab` is an extension on `StackNavigator<C>` that works like `bringToFront` but does **not** recreate the configuration if its class is already on the stack. Instead, it brings the existing instance to the front.

```kotlin
stackNavigation.switchTab(Config.Home)
```

## Passing Results Between Destinations

For passing results between destinations, see the `ResultFlow` API in the KMP Extras section.
