# Component Testing

The `decompose-test` module provides infrastructure for unit-testing Decompose components in Kotlin Multiplatform projects.

## Dependency

```kotlin
// commonMain test source set (or platform-specific test source set)
testImplementation("app.futured.arkitekt:decompose-test:{{ arkitekt.version }}")
```

For KMP modules, add it to the test source set in your `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonTest {
            dependencies {
                implementation("app.futured.arkitekt:decompose-test:{{ arkitekt.version }}")
            }
        }
    }
}
```

## ComponentTest

`ComponentTest` is an interface that wires up the testing environment for a `BaseComponent`:

| Member | Type | Description |
|--------|------|-------------|
| `testScope` | `TestScope` | Shared coroutine scope for the test body and `runComponentTest` |
| `testDispatcher` | `TestDispatcher` | Installed as `Dispatchers.Main` during each test |
| `lifecycleRegistry` | `LifecycleRegistry` | Drives the component lifecycle — call `lifecycleRegistry.create()` to start it |
| `componentContext` | `DefaultComponentContext` | Pass this to the component under test |
| `setup()` | `@BeforeTest` | Installs `testDispatcher` as `Dispatchers.Main` |
| `cleanup()` | `@AfterTest` | Destroys the lifecycle (if alive) and resets `Dispatchers.Main` |

## ComponentTestPreparation

`ComponentTestPreparation` is the default implementation. Use Kotlin class delegation to wire up your test class:

```kotlin
class LoginComponentTest : ComponentTest by ComponentTestPreparation() {

    @Test
    fun `state updates after login`() = runComponentTest {
        lifecycleRegistry.create()
        val component = LoginComponent(
            componentContext = componentContext,
            navigation = FakeLoginNavigation(),
            loginUseCase = FakeLoginUseCase(),
        )
        component.logIn()
        runCurrent()

        assertEquals("John Doe", component.state.value.fullName)
    }
}
```

`ComponentTestPreparation` is `open` — subclass it to add project-specific fixtures or helpers shared across multiple test classes.

## runComponentTest

`runComponentTest` is a helper that runs your test body inside `testScope`:

```kotlin
fun runComponentTest(
    timeout: Duration = 60.seconds,
    testBody: suspend TestScope.() -> Unit,
)
```

It delegates to `testScope.runTest(timeout, testBody)`. Use it as the test body launcher instead of `runTest` directly so the scope and dispatcher match the component under test.

## Driving the Lifecycle

The component lifecycle is advanced by calling Essenty lifecycle extensions on `lifecycleRegistry`:

```kotlin
lifecycleRegistry.create()   // INITIALIZED → CREATED
lifecycleRegistry.start()    // CREATED → STARTED
lifecycleRegistry.resume()   // STARTED → RESUMED
lifecycleRegistry.stop()     // RESUMED → STARTED
lifecycleRegistry.destroy()  // → DESTROYED (cancels lifecycleScope, closes events channel)
```

!!! note
    `cleanup()` automatically destroys the lifecycle after each test if it hasn't been destroyed yet.

## Testing State

Assert on `componentState` (exposed as a `StateFlow`) to verify state changes:

```kotlin
@Test
fun `initial state`() = runComponentTest {
    lifecycleRegistry.create()
    val component = createComponent()

    assertEquals(LoginState(), component.state.value)
}
```

!!! tip
    [Turbine](https://github.com/cashapp/turbine) provides a nicer API for testing `Flow` and `StateFlow` emissions, including `componentState` updates.

## Testing Events

Collect events inside `backgroundScope` to capture one-shot events:

```kotlin
@Test
fun `error event is emitted on failure`() = runComponentTest {
    lifecycleRegistry.create()
    val component = createComponent()

    val received = mutableListOf<LoginUiEvent>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        component.events.collect { received.add(it) }
    }

    component.logIn() // triggers an error
    runCurrent()

    assertEquals(listOf(LoginUiEvent.ShowError("Auth failed")), received)
}
```

## Testing Lifecycle Teardown

Verify that the component cleans up correctly on destroy:

```kotlin
@Test
fun `events flow completes on destroy`() = runComponentTest {
    val scope = CoroutineScope(testDispatcher + Job())
    val component = LoginComponent(componentContext, scope, FakeLoginNavigation(), FakeLoginUseCase())
    lifecycleRegistry.create()

    var completed = false
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        component.events.onCompletion { completed = true }.collect { }
    }

    lifecycleRegistry.destroy()

    assertTrue(completed)
}
```
