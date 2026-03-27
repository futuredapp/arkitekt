# Factory Generator (Koin)

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

## Generated Output

The annotation processor generates a factory object with a `createComponent()` method:

```kotlin
object HomeComponentFactory {
    fun createComponent(
        componentContext: AppComponentContext,
        navigation: HomeNavigation,
    ): HomeComponent {
        val koin = GlobalContext.get()
        return HomeComponent(
            componentContext = componentContext,
            navigation = navigation,
            someUseCase = koin.get(),
        )
    }
}
```

- Parameters marked with `@InjectedParam` become factory method parameters
- All other parameters are resolved via `koin.get()`

## KSP Configuration for KMP

!!! warning "Important"
    Use `kspCommonMainMetadata` only. Do **not** add the processor to platform-specific configurations (`kspAndroid`, `kspIosArm64`, etc.) as this would cause duplicate generation.

Add the following to your module's `build.gradle.kts`:

```kotlin
plugins {
    id("com.google.devtools.ksp")
}

dependencies {
    add("kspCommonMainMetadata", "app.futured.arkitekt:decompose-processor:{{ arkitekt.version }}")
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
