# Installation

All Arkitekt artifacts are published to **Maven Central** under the group ID `app.futured.arkitekt`.

## Requirements

| Tool | Minimum version |
|------|----------------|
| Android Gradle Plugin | 9.1 |
| Gradle | 9.4 |
| Kotlin | 2.4 |
| JDK | 17 |
| `minSdk` | 23 |

Arkitekt requires **AndroidX**. Jetifier is not supported.

## Dependencies

Add the modules you need to your `build.gradle.kts`:

```kotlin
dependencies {
    // Core ViewModel and ViewState classes (Android)
    implementation("app.futured.arkitekt:core:{{ arkitekt_version }}")

    // Jetpack Compose integration (Android)
    implementation("app.futured.arkitekt:compose:{{ arkitekt_version }}")

    // Coroutine use cases (KMP)
    implementation("app.futured.arkitekt:cr-usecases:{{ arkitekt_version }}")

    // Decompose integration (KMP)
    implementation("app.futured.arkitekt:decompose:{{ arkitekt_version }}")

    // KSP annotation for Decompose factory generation (KMP)
    implementation("app.futured.arkitekt:decompose-annotation:{{ arkitekt_version }}")

    // KSP processor for Decompose factory generation (KMP)
    ksp("app.futured.arkitekt:decompose-processor:{{ arkitekt_version }}")

    // ViewModel testing utilities
    testImplementation("app.futured.arkitekt:core-test:{{ arkitekt_version }}")

    // UseCase mocking utilities
    testImplementation("app.futured.arkitekt:cr-usecases-test:{{ arkitekt_version }}")

    // Decompose component testing utilities (KMP)
    testImplementation("app.futured.arkitekt:decompose-test:{{ arkitekt_version }}")
}
```

## Module selection

KMP modules support **Android and iOS** targets.

| Module | Android | iOS |
|---|:---:|:---:|
| `core` | ✓ | ✗ |
| `compose` | ✓ | ✗ |
| `cr-usecases` | ✓ | ✓ |
| `decompose` | ✓ | ✓ |
| `decompose-annotation` | ✓ | ✓ |
| `decompose-processor` | ✓ | ✓ |
| `core-test` | ✓ | ✗ |
| `cr-usecases-test` | ✓ | ✓ |
| `decompose-test` | ✓ | ✓ |

For **Android-only** projects, use `core`, `compose`, and `cr-usecases`.

For **KMP** projects, use `cr-usecases`, `decompose`, `decompose-annotation`, and `decompose-processor`.

## Snapshot versions

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
