# Migration Guide (5.x to 6.x)

Version 6.x is a major refactoring focused on modern Android development with Jetpack Compose and Kotlin Multiplatform.

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

## Migration Path

1. **Replace Fragment/Activity base classes** with `ComponentActivity` annotated with `@AndroidEntryPoint`
2. **Replace LiveData** with `StateFlow` (for ViewModels) or Compose `State` (for ViewState)
3. **Replace `LiveEvent`** with Channel-based `Event` system
4. **Migrate UI** to Jetpack Compose
5. **Replace `BaseViewModel`** (old core) with `BaseCoreViewModel` (core) or `BaseViewModel` (compose)
6. **Replace Dagger 2** with Dagger-Hilt
