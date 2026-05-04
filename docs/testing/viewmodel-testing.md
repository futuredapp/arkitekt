# ViewModel Testing

The `core-test` module provides a `ViewModelTest` base class that sets up the testing environment for Arkitekt ViewModels.

## Dependency

```kotlin
testImplementation("app.futured.arkitekt:core-test:{{ arkitekt.version }}")
```

## Setup

`ViewModelTest` configures:

- **`InstantTaskExecutorRule`** — makes LiveData operations synchronous
- **`CoroutineScopeRule`** — sets the `Main` dispatcher to `UnconfinedTestDispatcher` for immediate coroutine execution

## Basic Test

```kotlin
class DetailViewModelTest : ViewModelTest() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var viewState: DetailViewState

    @Before
    fun setUp() {
        viewState = DetailViewState()
        viewModel = DetailViewModel(viewState)
    }

    @Test
    fun `increment number updates state`() {
        viewModel.incrementNumber()
        assertEquals(1, viewState.number.value)
    }
}
```

## Testing Events

Use MockK's `spyk` to verify that events are sent:

```kotlin
viewModel = spyk(HomeViewModel(viewState), recordPrivateCalls = true)

viewModel.onDetail()
verify { viewModel.sendEvent(ShowDetailEvent) }
```

## Overriding the Worker Dispatcher

By default, use cases run on `Dispatchers.IO`. In tests, override `getWorkerDispatcher()` to run everything on the Main (test) dispatcher:

```kotlin
every { viewModel.getWorkerDispatcher() } returns Dispatchers.Main
```

This ensures use case execution is synchronous and predictable in tests.
