# UseCase Mocking

The `cr-usecases-test` module provides mock extensions for use cases. Since `execute` methods are extension functions on `CoroutineScopeOwner`, standard MockK mocking won't work. Use the provided utilities instead.

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
