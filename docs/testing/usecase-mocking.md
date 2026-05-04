# UseCase Mocking

!!! note "Android only"
    The utilities described on this page are provided by the `cr-usecases-test` module and are available on **Android only**. For KMP projects, see [KMP alternatives](#kmp-alternatives) below.

The `cr-usecases-test` module provides mock extensions for use cases. Since `execute` is a context parameter extension (not a regular method on the use case), standard MockK mocking won't work — the helpers work by mocking `build()` directly instead. Use the provided utilities.

## Dependency

```kotlin
testImplementation("app.futured.arkitekt:cr-usecases-test:{{ arkitekt.version }}")
```

## UseCase Mocking

```kotlin
val mockLoginUseCase: LoginUseCase = mockk()

// Mock with specific args
mockLoginUseCase.mockExecute(args = LoginData("John", "Doe")) { user }

// Mock with any args
mockLoginUseCase.mockExecute { user }

// Mock with nullable args
mockLoginUseCase.mockExecuteNullable(args = null) { user }
mockLoginUseCase.mockExecuteNullable { user }
```

## FlowUseCase Mocking

```kotlin
val mockObserveUseCase: ObserveUserUseCase = mockk()

// Mock with specific args
mockObserveUseCase.mockExecute(args = Unit) { flowOf("John Doe") }

// Mock with any args
mockObserveUseCase.mockExecute { flowOf("John Doe") }
```

## Full Test Example

```kotlin
class FormViewModelTest : ViewModelTest() {

    private val mockSaveUseCase: SaveFormUseCase = mockk()
    private val mockObserveUseCase: ObserveFormUseCase = mockk()
    private lateinit var viewState: FormViewState
    private lateinit var viewModel: FormViewModel

    @Before
    fun setUp() {
        viewState = FormViewState()
        mockObserveUseCase.mockExecute { flowOf("Alice" to "Smith") }
        mockSaveUseCase.mockExecute { "Alice" to "Smith" }
        viewModel = spyk(
            FormViewModel(mockSaveUseCase, mockObserveUseCase, viewState),
            recordPrivateCalls = true,
        ).also {
            every { it.getWorkerDispatcher() } returns Dispatchers.Main
        }
    }
}
```

This pattern ensures all use cases execute synchronously on the test dispatcher and return predictable results.

## KMP Alternatives

On KMP, use cases are plain interfaces, so the preferred approach is writing **fakes** — simple in-test implementations that return controlled results without any mocking framework.

```kotlin
class FakeLoginUseCase : LoginUseCase {
    var result: User = User("John", "Doe")

    override fun build(args: LoginData): User = result
}
```

If you prefer a mocking framework, [Mokkery](https://mokkery.dev/) supports Kotlin Multiplatform and can mock interfaces across all targets.

```kotlin
val mockLoginUseCase = mock<LoginUseCase>()

everySuspend { mockLoginUseCase.build(any()) } returns user
```

Refer to the [Mokkery documentation](https://mokkery.dev/) for setup and full API reference.
