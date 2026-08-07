# Factory generator (Koin)

The `@GenerateFactory` annotation triggers KSP code generation to create [Koin](https://insert-koin.io/) dependency injection factory objects for Decompose components. This eliminates the need to manually wire Koin dependencies when creating components in navigation factories.

## Usage

Annotate your component class with `@GenerateFactory`. Mark parameters that should be passed directly to the factory with `@InjectedParam`. All other constructor parameters are resolved from Koin automatically.

```kotlin
@GenerateFactory
class HomeComponent(
    @InjectedParam componentContext: AppComponentContext,
    @InjectedParam navigation: HomeNavigation,
    private val someUseCase: SomeUseCase, // resolved by Koin
) : BaseComponent<HomeState, HomeUiEvent>(componentContext, HomeState())
```

## Generated output

The annotation processor generates an `internal object` factory that implements `KoinComponent`:

```kotlin
internal object HomeComponentFactory : KoinComponent {
    fun createComponent(
        componentContext: AppComponentContext,
        navigation: HomeNavigation,
    ): HomeComponent = get(
        qualifier = null,
        parameters = { parametersOf(componentContext, navigation) },
    )
}
```

- Parameters marked with `@InjectedParam` become `createComponent()` parameters and are forwarded to Koin via `parametersOf`
- The component itself is resolved from Koin, so it must be registered in your Koin module (typically as a `factory`)
- All non-`@InjectedParam` constructor dependencies (e.g. `SomeUseCase`) are resolved by Koin from the DI graph

```kotlin
// Koin module
val homeModule = module {
    factory { params ->
        HomeComponent(
            componentContext = params.get(),
            navigation = params.get(),
            someUseCase = get(),
        )
    }
}
```

## Calling the factory

Use the generated factory inside a nav-host's `childStack` child factory:

```kotlin
val stack = childStack(
    source = navigation.stackNavigator,
    serializer = AppDestination.serializer(),
    initialConfiguration = AppDestination.Home,
    childFactory = { destination, ctx ->
        when (destination) {
            AppDestination.Home -> HomeComponentFactory.createComponent(ctx, navigation)
            AppDestination.Detail -> DetailComponentFactory.createComponent(ctx, navigation)
        }
    },
).asStateFlow()
```

`ctx` is the child `AppComponentContext` provided by Decompose; `navigation` is the nav-host's navigation instance passed through as an `@InjectedParam`.

## KSP configuration for KMP

!!! warning "Important"
    Use `kspCommonMainMetadata` only. Do **not** add the processor to platform-specific configurations (`kspAndroid`, `kspIosArm64`, etc.) as this would cause duplicate generation.

Add the following to your module's `build.gradle.kts`:

```kotlin
plugins {
    id("com.google.devtools.ksp")
}

dependencies {
    add("kspCommonMainMetadata", "app.futured.arkitekt:decompose-processor:{{ arkitekt_version }}")
}

kotlin.sourceSets.named("commonMain") {
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
```

This configuration ensures KSP runs on common metadata and the generated sources are available to all platform targets.
