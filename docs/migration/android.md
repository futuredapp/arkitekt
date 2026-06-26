# Android migration guide (5.x to 6.x)

Version 6.x is a major release focused on modern Android development with Jetpack Compose and Kotlin Coroutines.

## Toolchain requirements

Upgrade your project to meet the minimum requirements:

| Tool | Required version |
|------|-----------------|
| Android Gradle Plugin | 9.1 |
| Gradle | 9.4 |
| Kotlin | 2.4 |
| JDK | 17 |
| `compileSdk` / `targetSdk` | 36 |
| `minSdk` | 23 |

Remove multidex (not needed with `minSdk` ≥ 21) and remove Jetifier (`android.enableJetifier`) from `gradle.properties`. Arkitekt is AndroidX only.

!!! note "Hilt + Kotlin 2.4"

    Hilt currently bundles an older `kotlin-metadata-jvm` that may fail to parse Kotlin 2.4 metadata ([google/dagger#5001](https://github.com/google/dagger/issues/5001)). If you hit a metadata parsing error during the KSP/Hilt build, add the matching version as a KSP dependency so the highest version on the processor classpath wins:

    ```kotlin
    dependencies {
        ksp("org.jetbrains.kotlin:kotlin-metadata-jvm:2.4.0")
    }
    ```

    This is a temporary workaround until Hilt updates its bundled version.

## Removed modules

| Module | Replacement |
|---|---|
| `rx-usecases`, `rx-usecases-test` | `cr-usecases` (Coroutines) |
| `dagger` | Dagger-Hilt (`@HiltViewModel`, `@AndroidEntryPoint`) |
| `bindingadapters` | Jetpack Compose |
| `example-minimal`, `example-hilt` | Consolidated `example` module |

## Removed classes

### ViewModel base classes

| Removed | Replacement |
|---|---|
| `BaseViewModel` (from core) | `BaseCoreViewModel` (core) or `BaseViewModel` (compose) |
| `BaseCrViewModel` (from cr-usecases) | `BaseViewModel` (compose) |

### Fragment / Activity base classes

| Removed | Replacement |
|---|---|
| `ViewModelActivity`, `BindingViewModelActivity` | `ComponentActivity` with `@AndroidEntryPoint` |
| `ViewModelFragment`, `BindingViewModelFragment` | Compose navigation |
| `ViewModelBottomSheetDialogFragment`, `BindingViewModelBottomSheetDialogFragment` | Compose bottom sheets |
| `ViewModelDialogFragment`, `BindingViewModelDialogFragment` | Compose dialogs |

### Dagger classes

| Removed | Replacement |
|---|---|
| `BaseViewModelFactory`, `BaseSavedStateViewModelFactory` | `@HiltViewModel` with `hiltViewModel()` |
| `BaseDaggerActivity`, `BaseDaggerFragment` and Binding variants | `@AndroidEntryPoint` |
| `ViewModelCreator`, `ViewModelFactory` | No longer needed with Hilt |

### LiveData components

| Removed | Replacement |
|---|---|
| `LiveEvent`, `LiveEventBus` | `Event` with Channel-based events |
| `DefaultValueLiveData`, `DefaultValueMediatorLiveData` | `StateFlow` or Compose `State` |
| `NonNullLiveData` | `StateFlow` or Compose `State` |
| `UiData`, `UiDataExtensions`, `UiDataMediator` | `StateFlow` or Compose `State` |
| `LiveDataExtensions`, `LiveDataUtils` | Kotlin Flow operators |

### DataBinding

All DataBinding support has been removed. Use Jetpack Compose instead.

## UseCase API changes

### UseCase is now an interface

`UseCase<ARGS, T>` changed from an abstract class to an interface. Update all definitions:

```kotlin
// Before
class LoginUseCase : UseCase<LoginData, User>() { ... }

// After
class LoginUseCase : UseCase<LoginData, User> { ... }
```

The `deferred` property that tracked cancellation has been removed. Cancellation is now managed internally via `CoroutineScopeOwner.useCaseJobPool`.

### FlowUseCase is now an interface

Same change applies to `FlowUseCase`:

```kotlin
// Before
class ObserveUserUseCase : FlowUseCase<Unit, User>() { ... }

// After
class ObserveUserUseCase : FlowUseCase<Unit, User> { ... }
```

The `job` property has been removed. The running job is stored in `useCaseJobPool`.

### executeMapped removed

`executeMapped` has been removed from `FlowUseCase`. Apply transformations inside `build()` instead:

```kotlin
// Before
flowUseCase.executeMapped(Unit, transform = { it.name }) {
    onNext { name -> /* ... */ }
}

// After
class ObserveUserNamesUseCase : FlowUseCase<Unit, String> {
    override fun build(args: Unit): Flow<String> =
        userStore.observeUsers().map { it.name }
}
```

### execute uses context parameters

The `execute` extensions are now declared with Kotlin context parameters. Context parameters are stable as of Kotlin 2.4, so no compiler flag is needed. Call `execute(...)` directly from inside any `CoroutineScopeOwner` (e.g. a ViewModel or Component) and the receiver is resolved automatically. The call sites remain syntactically the same as before:

```kotlin
loginUseCase.execute(args) { onSuccess { /* ... */ } }
```

## CoroutineScopeOwner changes

### coroutineScope renamed to useCaseScope

The `coroutineScope` property on `CoroutineScopeOwner` has been renamed to `useCaseScope`. Update any custom `CoroutineScopeOwner` implementations:

```kotlin
// Before
override val coroutineScope: CoroutineScope = viewModelScope

// After
override val useCaseScope: CoroutineScope = viewModelScope
```

### useCaseJobPool is now required

Custom `CoroutineScopeOwner` implementations must also provide `useCaseJobPool`:

```kotlin
override val useCaseJobPool: MutableMap<Any, Job> = mutableMapOf()
```

`BaseViewModel` and `BaseCoreViewModel` from Arkitekt already provide both, so only custom implementations need to be updated.

## Result type migration

Arkitekt's custom `Result<VALUE>` sealed class has been removed. The sync-execute variant now returns `kotlin.Result<T>`.

### Replacing extension functions

| Old (Arkitekt Result) | New (kotlin.Result) |
|---|---|
| `.getOrNull()` | `.getOrNull()` ✓ built-in |
| `.getOrDefault(v)` | `.getOrDefault(v)` ✓ built-in |
| `.getOrElse { }` | `.getOrElse { }` ✓ built-in |
| `.getOrThrow()` | `.getOrThrow()` ✓ built-in |
| `.getOrCancel { }` | `.getOrCancel { }` from `cr-usecases` |
| `.map { }` | `.map { }` ✓ built-in |
| `.fold(onSuccess, onError)` | `.fold(onSuccess, onFailure)` ✓ built-in |
| `tryCatch { }` | `runCatching { }` ✓ built-in |

### Destructuring

`kotlin.Result` does not support destructuring declarations. Replace any `val (value, error) = result` calls with explicit `getOrNull()` / `exceptionOrNull()` calls.

## Migration path

1. **Upgrade toolchain** to AGP 9.1, Gradle 9.4, Kotlin 2.4, JDK 17
2. **Remove multidex and Jetifier** from `gradle.properties`
3. **Add `ksp("org.jetbrains.kotlin:kotlin-metadata-jvm:2.4.0")`** if you hit Hilt metadata parsing errors ([google/dagger#5001](https://github.com/google/dagger/issues/5001))
4. **Replace Fragment/Activity base classes** with `ComponentActivity` + `@AndroidEntryPoint`
5. **Replace LiveData** with `StateFlow` or Compose `State`
6. **Replace `LiveEvent`** with Channel-based `Event` system
7. **Migrate UI** to Jetpack Compose
8. **Replace old `BaseViewModel`** with `BaseCoreViewModel` or `BaseViewModel`
9. **Replace Dagger 2** with Dagger-Hilt
10. **Update UseCase/FlowUseCase** from abstract class to interface (remove `()` from supertype)
11. **Remove `executeMapped` calls** and apply mapping in `build()` instead
12. **Rename `coroutineScope` → `useCaseScope`** in custom `CoroutineScopeOwner` implementations; add `useCaseJobPool`
13. **Replace custom `Result` usage** with `kotlin.Result` extensions
