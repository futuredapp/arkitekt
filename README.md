<img align="right" src="extras/MMVM_Android.svg">

# Arkitekt

[![Download](https://img.shields.io/maven-central/v/app.futured.arkitekt/core)](https://search.maven.org/search?q=app.futured.arkitekt)
[![Build Status](https://github.com/futuredapp/arkitekt/workflows/Check%20PR/badge.svg)](https://github.com/futuredapp/arkitekt/actions)

Arkitekt is a set of architectural tools based on Android Architecture Components, which gives you a solid base to implement the concise, testable and solid application.

# Installation

```groovy
dependencies {
    implementation("app.futured.arkitekt:core:LatestVersion")
    implementation("app.futured.arkitekt:cr-usecases:LatestVersion")
    implementation("app.futured.arkitekt:compose:LatestVersion")
    implementation("app.futured.arkitekt:arkitekt-decompose:LatestVersion")
    
    // Testing
    testImplementation("app.futured.arkitekt:core-test:LatestVersion")
    testImplementation("app.futured.arkitekt:cr-usecases-test:LatestVersion")
}
```

## Snapshot installation

Add new maven repo to your top level gradle file.

```
maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
```

Snapshots are grouped based on major version, so for version 6.x use:

```groovy
implementation "app.futured.arkitekt:core:6.X.X-SNAPSHOT"
implementation "app.futured.arkitekt:cr-usecases:6.X.X-SNAPSHOT"
implementation "app.futured.arkitekt:compose:6.X.X-SNAPSHOT"
implementation "app.futured.arkitekt:arkitekt-decompose:6.X.X-SNAPSHOT"
```

# Features

Arkitekt is a modern Android architecture library focused on Jetpack Compose and Kotlin Coroutines. 
It combines built-in support for Dagger-Hilt dependency injection, ViewModel, Coroutines use cases, 
Jetpack Compose, and Decompose. 

**Note:** As of version 6.x, Arkitekt has removed legacy LiveData-based components and Fragment/Activity 
base classes. The library is now exclusively focused on Jetpack Compose with State/StateFlow for reactive UI.

![MVVM architecture](extras/architecture-diagram.png)

# Migration Guide (5.x → 6.x)

Version 6.x represents a major refactoring focused on modern Android development with Jetpack Compose. 
The following legacy components have been removed:

### Removed Classes

**ViewModel Base Classes:**
- `BaseLegacyCoreViewModel` - Use `BaseCoreViewModel` or `BaseViewModel` instead
- `BaseLegacyViewModel` (from cr-usecases) - Use `BaseViewModel` instead

**Fragment/Activity Base Classes:**
- `ViewModelActivity` - Use standard `ComponentActivity` with `@AndroidEntryPoint`
- `ViewModelFragment` - Use standard Compose navigation
- `ViewModelBottomSheetDialogFragment` - Use Compose bottom sheets
- `ViewModelDialogFragment` - Use Compose dialogs

**LiveData Components:**
- `LiveEvent` and `LiveEventBus` - Use `Event` with `Channel`-based events
- `DefaultValueLiveData` and `DefaultValueMediatorLiveData` - Use `StateFlow` or Compose `State`
- `NonNullLiveData` - Use `StateFlow` or Compose `State`
- `UiData`, `UiDataExtensions`, `UiDataMediator` - Use `StateFlow` or Compose `State`
- `LiveDataExtensions` and `LiveDataUtils` - Use Kotlin Flow operators

### Migration Path

1. **Replace Fragment/Activity base classes** with standard Android components annotated with `@AndroidEntryPoint`
2. **Replace LiveData** with `StateFlow` (for ViewModels) or `State` (for Compose)
3. **Replace LiveEvent** with Channel-based `Event` system (see [Events](#events) section)
4. **Migrate to Jetpack Compose** for UI layer

# Usage

## Table of contents

1. [Getting started - Minimal project file hierarchy](#getting-started---minimal-project-file-hierarchy)
2. [Use Cases](#use-cases)
3. [Propagating data model changes into UI](#propagating-data-model-changes-into-ui)
4. [Stores (Repositories)](#stores-repositories)

## Getting Started - Minimal project file hierarchy
Minimal working project must contain files as presented in `example`
module. File hierarchy might look like this:
```
example
`-- src/main
    |-- java/app/futured/arkitekt/sample
    |   |-- ui 
    |   |   |-- main
    |   |   |   `-- MainActivity.kt
    |   |   `-- home
    |   |       |-- HomeScreen.kt
    |   |       |-- HomeViewModel.kt
    |   |       `-- HomeViewState.kt
    |   `-- App.kt 
    `-- res/values/strings.xml  
```

Let's describe individual files one by one:

##### `App.kt`
Application class must be annotated with `@HiltAndroidApp` to trigger Hilt code generation. Optionally set `UseCaseErrorHandler.globalOnErrorLogger` for application-wide error logging in use cases.

```kotlin
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        UseCaseErrorHandler.globalOnErrorLogger = { error ->
            android.util.Log.d("UseCase error", "$error")
        }
    }
}
``` 

##### `MainActivity.kt`

Activity must be annotated with `@AndroidEntryPoint`. We use `setContent` to define the UI using Jetpack Compose. For a single screen, use `HomeScreen()` directly. For multiple screens, use a `NavHost` with your navigation graph (see [example MainActivity](example/src/main/java/app/futured/arkitekt/sample/ui/main/MainActivity.kt)).

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArkitektTheme {
                 // Your navigation or single screen
                 HomeScreen()
            }
        }
    }
}
```

##### `HomeViewModel.kt`

ViewModel annotated with `@HiltViewModel`. You can choose between extending
`BaseCoreViewModel` or `BaseViewModel` (for Coroutines support).

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor() : BaseCoreViewModel<HomeViewState>() {

    override val viewState = HomeViewState()
}
```

##### `HomeViewState.kt`

State representation of a screen. Should contain a set of `State` (Compose) or `StateFlow` fields observed by the UI.

```kotlin
class HomeViewState @Inject constructor() : ViewState {
    val user = mutableStateOf(User.EMPTY)
}
```

##### `HomeScreen.kt`

Composable function representing the UI. It obtains the ViewModel via Hilt injection.

```kotlin
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val user by viewModel.viewState.user
    
    // UI implementation - User has firstName and lastName
    Text(
        text = "${user.firstName} ${user.lastName}".trim().ifEmpty { "Guest" },
        modifier = modifier
    )
}
```

## Use Cases

Module `cr-usecases` contains base classes useful for easy execution of
background tasks based on Coroutines. Two base types are available - `UseCase` (single result use case)
and `FlowUseCase` (multi result use case).

Following example describes how to make an API call and how to deal with 
result of this call. 

##### LoginUseCase.kt
```kotlin
class LoginUseCase @Inject constructor(
    private val userStore: UserStore
) : UseCase<LoginData, Unit>() {

    override suspend fun build(args: LoginData) {
        userStore.setUser(User(args.firstName, args.lastName))
    }
}

data class LoginData(val firstName: String, val lastName: String)
```
##### LoginViewState.kt
```kotlin
class LoginViewState @Inject constructor() : ViewState {
    // IN - values provided by UI
    val name = mutableStateOf("")
    val surname = mutableStateOf("")

    // OUT - Values observed by UI
    val fullName = mutableStateOf("")
    val isLoading = mutableStateOf(false)
}
```

##### LoginViewModel.kt
```kotlin
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase, // Inject UseCase
    override val viewState: LoginViewState
) : BaseViewModel<LoginViewState>() {

    fun logIn() = with(viewState) {
        loginUseCase.execute(LoginData(name.value, surname.value)) {
            onStart {
                isLoading.value = true
            }
            onSuccess {
                isLoading.value = false
                fullName.value = "${viewState.name.value} ${viewState.surname.value}"
            }
            onError {
                isLoading.value = false
                // handle error
            }
        }
    }
}
```

### Synchronous execution of cr-usecase

Module `cr-usecases` allows you to execute use cases synchronously. 
```kotlin
fun onButtonClicked() = launchWithHandler {  
    // ...
    val data = useCase.execute().getOrDefault("Default")  
    // ...
}
```
`execute` method returns a `Result` that can be either successful `Success` or failed `Error`.

`launchWithHandler` launches a new coroutine encapsulated with a try-catch block. By default exception thrown in `launchWithHandler` is rethrown but it is possible to override this behavior with `defaultErrorHandler` or just log these exceptions in `logUnhandledException`.

### Global error logger for handled errors in use-cases

In order to set an application-wide error logger for all handled errors in all use-cases, it is possible to set the following method in the `Application` class:

```kotlin
UseCaseErrorHandler.globalOnErrorLogger = { error ->
    CustomLogger.logError(error)
}
```

The `globalOnErrorLogger` callback in the `UseCaseErrorHandler` will be called for every error thrown in all use-cases that have defined onError receiver in the execute method.

The following execute method will trigger `globalOnErrorLogger`:

```kotlin
useCase.execute {
    ...
    onError {
        isLoading = false
    }
    ...
}
```

The following execute method won't trigger `globalOnErrorLogger` because onError is not defined and execute method will throw an unhandled exception.

```kotlin
useCase.execute {}
```

## Propagating data model changes into UI
There are two main ways how to reflect data model changes in UI. Through `ViewState` observation
or one-shot `Events`. 

### ViewState observation

You can observe state changes and reflect these changes in UI by observing `State` (Compose) or `StateFlow`
from your `viewState` in your Composable:

```kotlin
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val myText by viewModel.viewState.myTextState
    
    Text(text = myText)
}
```

### Events
Events are one-shot messages sent from `ViewModel` to a Composable. They
are based on `Channel`. Events are guaranteed to be delivered only once even when
there is screen rotation in progress. Basic event communication might look like this:

If you are using Jetpack Compose, you can collect these events via `EventsEffect`:
```kotlin
viewModel.EventsEffect {
    onEvent<ShowDetailEvent> { /* handle event */ }
}
```

##### `HomeEvents.kt`
```kotlin
sealed class HomeEvent : Event<HomeViewState>()

object ShowDetailEvent : HomeEvent()
```

##### `HomeViewModel.kt`
```kotlin
class HomeViewModel @Inject constructor(
    override val viewState: HomeViewState
) : BaseCoreViewModel<HomeViewState>() {

    fun onDetail() {
        sendEvent(ShowDetailEvent)
    }
}
```

## Stores (Repositories)
All our applications respect broadly known repository pattern. The main message this
pattern tells: Define `Store` (Repository) classes with single entity related business logic 
eg. `UserStore`, `OrderStore`, `DeviceStore` etc. Let's see this principle on `UserStore` class
from sample app:

##### `UserStore.kt`
```kotlin
@Singleton
class UserStore @Inject constructor() {
    private val userState = MutableStateFlow(User.EMPTY)

    fun setUser(user: User) {
        userState.value = user
        // ... optionally persist user
    }

    fun getUser(): StateFlow<User> = userState
}
```

With this approach only one class is responsible for `User` related data access. Besides 
custom classes, Room library `Dao`s or for example Retrofit API interfaces might be 
perceived on the same domain level as stores. Thanks to use cases we can easily access, 
manipulate and combine this kind of data on background threads. 

```kotlin
class ObserveUserFullNameUseCase @Inject constructor(
    private val userStore: UserStore
) : FlowUseCase<Unit, String>() {

    override fun build(args: Unit): Flow<String> =
        userStore.getUser().map { "${it.firstName} ${it.lastName}" }
}
```

We strictly respect this injection hierarchy:

| Application Component | Injects |
| --------- | --------------------- |
| `Composable` | `ViewModel` |
| `ViewModel` | `ViewState`, `UseCase` |
| `UseCase` | `Store` |
| `Store` | `Dao`, `Persistence`, `ApiService` |

## Navigation

Arkitekt supports two modern navigation approaches:

### Native Android Navigation (Jetpack Compose)
You can use the standard Jetpack Navigation component with Compose. 

### Decompose (Kotlin Multiplatform)
For KMP projects or robust state management, `arkitekt-decompose` provides integration with the Decompose library. This allows sharing navigation logic across platforms.

## SavedStateHandle

Arkitekt supports `SavedStateHandle` in `ViewModel` via Hilt standard mechanism. Simply inject `SavedStateHandle` into your `@HiltViewModel`.

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<HomeViewState>() {
    // uses savedStateHandle
}
```

## Testing

In order to create successful applications, it is highly encouraged to write tests for your application. 

See [these tests](https://github.com/futuredapp/arkitekt/tree/main/example/src/) in `example` module for more detailed sample.

### ViewModel testing

[core-test](#Download) dependency contains utilities to help you with ViewModel testing.

`ViewModelTest` can be used as a base class for view model tests inside `core-test` module to help with Coroutines testing.

### Events testing

The [spy](https://github.com/mockk/mockk#spy) object should be used for an easy way of testing that expected events were sent to the view.

```kotlin
viewModel = spyk(SampleViewModel(mockViewState, ...), recordPrivateCalls = true)
...
verify { viewModel.sendEvent(ExpectedEvent) }
```

### Mocking of Use Cases

[cr-usecase-test](#Download) dependency contains utilities to help you with mocking use cases in a view model.

Since all 'execute' methods for [use cases](#use-cases) are implemented as extension functions, we created testing methods that will help you to easily mock them.

So if a method in the view model looks somehow like this:
```kotlin
fun onLoginClicked(name: String, password: String) {
    loginUseCase.execute(LoginData(name, password)) {
        onSuccess = { ... }
    }
}
```
then it can be mocked with the following method:
```kotlin
mockLoginUseCase.mockExecute(args = ...) { user } // For Coroutines Use Cases
```
In case that use case is using nullable arguments:
```kotlin
mockLoginUseCase.mockExecuteNullable(args = ...) { user } // For Coroutines Use Cases
```

### Compose tests

If you want to test your UI, you can use standard Compose testing APIs (`createComposeRule`).

```kotlin
@HiltAndroidTest
class HomeScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testUI() {
        composeTestRule.onNodeWithText("Hello").assertIsDisplayed()
    }
}
```

# License
Arkitekt is available under the MIT license. See the [LICENSE file](LICENCE) for more information.

Created with &#x2764; at Futured. Inspired by [Alfonz library](https://github.com/petrnohejl/Alfonz).
