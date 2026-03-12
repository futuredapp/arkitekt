# Installation

All Arkitekt artifacts are published to **Maven Central** under the group ID `app.futured.arkitekt`.

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
}
```

## Module Selection

| Module | Android-only | KMP |
|---|:---:|:---:|
| `core` | Yes | — |
| `compose` | Yes | — |
| `cr-usecases` | Yes | Yes |
| `decompose` | — | Yes |
| `decompose-annotation` | — | Yes |
| `decompose-processor` | — | Yes |
| `core-test` | Yes | — |
| `cr-usecases-test` | Yes | Yes |

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
