# Project Setup — Android

This guide walks through the minimal project structure for an Android app using Arkitekt with Jetpack Compose and Hilt.

## Project Structure

```
app/
└── src/main
    ├── java/com/example/myapp
    │   ├── ui
    │   │   ├── main
    │   │   │   └── MainActivity.kt
    │   │   └── home
    │   │       ├── HomeScreen.kt
    │   │       ├── HomeViewModel.kt
    │   │       └── HomeViewState.kt
    │   └── App.kt
    └── res/values/strings.xml
```

## Build Configuration

!!! note "Hilt + Kotlin 2.4"

    Hilt currently bundles an older `kotlin-metadata-jvm` that may fail to parse Kotlin 2.4 metadata ([google/dagger#5001](https://github.com/google/dagger/issues/5001)). If you hit a metadata parsing error during the KSP/Hilt build, add the matching version as a KSP dependency so the highest version on the processor classpath wins:

    ```kotlin
    dependencies {
        ksp("org.jetbrains.kotlin:kotlin-metadata-jvm:2.4.0")
    }
    ```

    This is a temporary workaround until Hilt updates its bundled version.

## Application Class

Create an `Application` subclass annotated with `@HiltAndroidApp`. You can optionally configure global error logging for use cases here.

```kotlin
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        UseCaseErrorHandler.globalOnErrorLogger = { error ->
            Log.d("UseCase", "$error")
        }
    }
}
```

## Activity

The main activity serves as the Compose entry point. Annotate it with `@AndroidEntryPoint` for Hilt injection.

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                HomeScreen()
            }
        }
    }
}
```

## ViewState

ViewState holds the UI state as observable Compose `mutableStateOf` fields. It implements the `ViewState` interface from the `core` module.

```kotlin
class HomeViewState @Inject constructor() : ViewState {
    val title = mutableStateOf("")
}
```

## ViewModel

The ViewModel extends `BaseCoreViewModel` and exposes the view state. Annotate with `@HiltViewModel` for Hilt injection.

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    override val viewState: HomeViewState,
) : BaseCoreViewModel<HomeViewState>()
```

## Screen Composable

The screen composable obtains the ViewModel via `hiltViewModel()` and reads state directly from the view state fields.

```kotlin
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val title by viewModel.viewState.title
    Text(text = title)
}
```
