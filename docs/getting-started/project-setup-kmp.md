# Project Setup — KMP

This guide walks through the minimal project structure for a Kotlin Multiplatform project using Arkitekt with Decompose for component-based architecture and Koin for dependency injection.

## Project Structure

```
shared/
└── src/commonMain/kotlin/com/example/myapp
    ├── navigation
    │   └── HomeNavigation.kt
    └── ui/home
        ├── HomeComponent.kt
        └── HomeState.kt

androidApp/
└── src/main/kotlin/com/example/myapp
    └── ui/home
        └── HomeScreen.kt
```

## State

State is a simple data class defined in common code. It represents the UI state for a given component.

```kotlin
data class HomeState(val title: String = "")
```

## Navigation

Navigation actions are defined as an interface extending `NavigationActions`. The parent component provides the implementation.

```kotlin
interface HomeNavigation : NavigationActions {
    fun toDetail()
}
```

## Component

The component extends `BaseComponent` and is annotated with `@GenerateFactory` to enable automatic factory generation via KSP.

```kotlin
@GenerateFactory
class HomeComponent(
    @InjectedParam componentContext: AppComponentContext,
    @InjectedParam navigation: HomeNavigation,
    private val someUseCase: SomeUseCase,
) : BaseComponent<HomeState, HomeUiEvent>(componentContext, HomeState()) {

    val state: StateFlow<HomeState> = componentState

    fun onDetailClicked() {
        navigation.toDetail()
    }
}
```

The `@GenerateFactory` annotation triggers KSP to generate a `HomeComponentFactory` class. This factory resolves Koin dependencies (such as `someUseCase`) automatically — you only need to supply `@InjectedParam` arguments manually when creating the component.

For full KSP setup details and advanced configuration, see the [Factory Generator](../architecture/kmp/factory-generator.md) page.

## Screen Composable (Android)

On the Android target, the screen composable receives the component directly and collects its state as a Compose state.

```kotlin
@Composable
fun HomeScreen(component: HomeComponent) {
    val state by component.state.collectAsState()
    Text(text = state.title)

    EventsEffect(component.events) {
        onEvent<HomeUiEvent.ShowToast> { /* handle */ }
    }
}
```
