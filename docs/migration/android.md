# Migration Guide — Android (5.x to 6.x)

Version 6.x is a major release focused on modern Android development with Jetpack Compose and Kotlin Coroutines.

## Toolchain Requirements

Upgrade your project to meet the minimum requirements:

| Tool | Required version |
|------|-----------------|
| Android Gradle Plugin | 9.1 |
| Gradle | 9.4 |
| Kotlin | 2.3 |
| JDK | 17 |
| `compileSdk` / `targetSdk` | 36 |
| `minSdk` | 23 |

Remove multidex (not needed with `minSdk` ≥ 21) and remove Jetifier (`android.enableJetifier`) from `gradle.properties` — Arkitekt is AndroidX-only.

### Enable Context Parameters

Add the compiler flag to every module that calls `UseCase.execute(...)` or `FlowUseCase.execute(...)`:

```kotlin
android {
    kotlinOptions {
        freeCompilerArgs += "-Xcontext-parameters"
    }
}
```

## Removed Modules

| Module | Replacement |
|---|---|
| `rx-usecases`, `rx-usecases-test` | `cr-usecases` (Coroutines) |
| `dagger` | Dagger-Hilt (`@HiltViewModel`, `@AndroidEntryPoint`) |
| `bindingadapters` | Jetpack Compose |
| `example-minimal`, `example-hilt` | Consolidated `example` module |

## Removed Classes

### ViewModel Base Classes

| Removed | Replacement |
|---|---|
| `BaseViewModel` (from core) | `BaseCoreViewModel` (core) or `BaseViewModel` (compose) |
| `BaseCrViewModel` (from cr-usecases) | `BaseViewModel` (compose) |

### Fragment / Activity Base Classes

| Removed | Replacement |
|---|---|
| `ViewModelActivity`, `BindingViewModelActivity` | `ComponentActivity` with `@AndroidEntryPoint` |
| `ViewModelFragment`, `BindingViewModelFragment` | Compose navigation |
| `ViewModelBottomSheetDialogFragment`, `BindingViewModelBottomSheetDialogFragment` | Compose bottom sheets |
| `ViewModelDialogFragment`, `BindingViewModelDialogFragment` | Compose dialogs |

### Dagger Classes

| Removed | Replacement |
|---|---|
| `BaseViewModelFactory`, `BaseSavedStateViewModelFactory` | `@HiltViewModel` with `hiltViewModel()` |
| `BaseDaggerActivity`, `BaseDaggerFragment` and Binding variants | `@AndroidEntryPoint` |
| `ViewModelCreator`, `ViewModelFactory` | No longer needed with Hilt |

### LiveData Components

| Removed | Replacement |
|---|---|
| `LiveEvent`, `LiveEventBus` | `Event` with Channel-based events |
| `DefaultValueLiveData`, `DefaultValueMediatorLiveData` | `StateFlow` or Compose `State` |
| `NonNullLiveData` | `StateFlow` or Compose `State` |
| `UiData`, `UiDataExtensions`, `UiDataMediator` | `StateFlow` or Compose `State` |
| `LiveDataExtensions`, `LiveDataUtils` | Kotlin Flow operators |

### DataBinding

All DataBinding support has been removed. Use Jetpack Compose instead.

## UseCase API Changes

### UseCase is now an interface

`UseCase<ARGS, T>` changed from an abstract class to an interface. Update all definitions:

```kotlin
// Before
class LoginUseCase : UseCase<LoginData, User>() { ... }

// After
class LoginUseCase : UseCase<LoginData, User> { ... }
```

The `deferred` property that tracked cancellation has been removed — cancellation is now managed internally via `CoroutineScopeOwner.useCaseJobPool`.

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

The `execute` extensions are now declared with Kotlin context parameters. If your project enables `-Xcontext-parameters`, the call sites remain syntactically the same. Without the flag, use the deprecated explicit-owner overload:

```kotlin
// With -Xcontext-parameters (recommended — same syntax as before from inside a CoroutineScopeOwner)
loginUseCase.execute(args) { onSuccess { /* ... */ } }

// Without flag (deprecated overload)
loginUseCase.execute(args, coroutineScopeOwner = this) { onSuccess { /* ... */ } }
```

## CoroutineScopeOwner Changes

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

`BaseViewModel` and `BaseCoreViewModel` from Arkitekt already provide both — only custom implementations need to be updated.

## Result Type Migration

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

## Migration Path

1. **Upgrade toolchain** to AGP 9.1, Gradle 9.4, Kotlin 2.3, JDK 17
2. **Remove multidex and Jetifier** from `gradle.properties`
3. **Add `-Xcontext-parameters`** compiler flag to affected modules
4. **Replace Fragment/Activity base classes** with `ComponentActivity` + `@AndroidEntryPoint`
5. **Replace LiveData** with `StateFlow` or Compose `State`
6. **Replace `LiveEvent`** with Channel-based `Event` system
7. **Migrate UI** to Jetpack Compose
8. **Replace old `BaseViewModel`** with `BaseCoreViewModel` or `BaseViewModel`
9. **Replace Dagger 2** with Dagger-Hilt
10. **Update UseCase/FlowUseCase** from abstract class to interface (remove `()` from supertype)
11. **Remove `executeMapped` calls** — apply mapping in `build()` instead
12. **Rename `coroutineScope` → `useCaseScope`** in custom `CoroutineScopeOwner` implementations; add `useCaseJobPool`
13. **Replace custom `Result` usage** with `kotlin.Result` extensions
