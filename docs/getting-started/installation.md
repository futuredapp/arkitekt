# Installation

All Arkitekt artifacts are published to **Maven Central** under the group ID `app.futured.arkitekt`.

## Requirements

| Tool | Minimum version |
|------|----------------|
| Android Gradle Plugin | 9.1 |
| Gradle | 9.4 |
| Kotlin | 2.3 |
| JDK | 17 |
| `minSdk` | 23 |

Arkitekt requires **AndroidX** — Jetifier is not supported.

### Context Parameters Compiler Flag

The `execute` extension functions on `UseCase` and `FlowUseCase` use Kotlin context parameters. Add the following compiler option to any module that calls `execute(...)`:

=== "Android (build.gradle.kts)"

    ```kotlin
    android {
        kotlinOptions {
            freeCompilerArgs += "-Xcontext-parameters"
        }
    }
    ```

=== "KMP (build.gradle.kts)"

    ```kotlin
    kotlin {
        compilerOptions {
            freeCompilerArgs.add("-Xcontext-parameters")
        }
    }
    ```

Projects that cannot enable this flag can use the deprecated explicit-owner overloads — see [UseCase](../use-cases/usecase.md) for details.

## Dependencies

Add the modules you need to your `build.gradle.kts`:

```kotlin
dependencies {
    // Core ViewModel and ViewState classes (Android)
    implementation("app.futured.arkitekt:core:{{ arkitekt.version }}")

    // Jetpack Compose integration (Android)
    implementation("app.futured.arkitekt:compose:{{ arkitekt.version }}")

    // Coroutine use cases (KMP)
    implementation("app.futured.arkitekt:cr-usecases:{{ arkitekt.version }}")

    // Decompose integration (KMP)
    implementation("app.futured.arkitekt:decompose:{{ arkitekt.version }}")

    // KSP annotation for Decompose factory generation (KMP)
    implementation("app.futured.arkitekt:decompose-annotation:{{ arkitekt.version }}")

    // KSP processor for Decompose factory generation (KMP)
    ksp("app.futured.arkitekt:decompose-processor:{{ arkitekt.version }}")

    // ViewModel testing utilities
    testImplementation("app.futured.arkitekt:core-test:{{ arkitekt.version }}")

    // UseCase mocking utilities
    testImplementation("app.futured.arkitekt:cr-usecases-test:{{ arkitekt.version }}")

    // Decompose component testing utilities (KMP)
    testImplementation("app.futured.arkitekt:decompose-test:{{ arkitekt.version }}")
}
```

## Module Selection

KMP modules support **Android and iOS** targets.

| Module | Android | iOS |
|---|:---:|:---:|
| `core` | ✓ | — |
| `compose` | ✓ | — |
| `cr-usecases` | ✓ | ✓ |
| `decompose` | ✓ | ✓ |
| `decompose-annotation` | ✓ | ✓ |
| `decompose-processor` | ✓ | ✓ |
| `core-test` | ✓ | — |
| `cr-usecases-test` | ✓ | ✓ |
| `decompose-test` | ✓ | ✓ |

For **Android-only** projects, use `core`, `compose`, and `cr-usecases`.

For **KMP** projects, use `cr-usecases`, `decompose`, `decompose-annotation`, and `decompose-processor`.

## Snapshot Versions

Snapshot builds are published to the Sonatype snapshots repository. To use them, add the snapshot repository to your `settings.gradle.kts` or `build.gradle.kts`:

```kotlin
repositories {
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
}
```

Snapshots are grouped by major version. For example, development builds for the 6.x release line are published as:

```kotlin
implementation("app.futured.arkitekt:core:6.X.X-SNAPSHOT")
```

Replace `6.X.X-SNAPSHOT` with the actual snapshot version available in the repository.
