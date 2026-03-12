<img align="right" src="extras/MMVM_Android.svg">

# Arkitekt

[![Download](https://img.shields.io/maven-central/v/app.futured.arkitekt/core)](https://search.maven.org/search?q=app.futured.arkitekt)
[![Build Status](https://github.com/futuredapp/arkitekt/workflows/Check%20PR/badge.svg)](https://github.com/futuredapp/arkitekt/actions)

Arkitekt is a modern Android & Kotlin Multiplatform architecture library built on Jetpack Compose and Kotlin Coroutines.

**[Read the full documentation at arkitekt.futured.app](https://arkitekt.futured.app)**

## Installation

```kotlin
dependencies {
    // Android / Compose
    implementation("app.futured.arkitekt:core:LatestVersion")
    implementation("app.futured.arkitekt:cr-usecases:LatestVersion")
    implementation("app.futured.arkitekt:compose:LatestVersion")

    // Decompose / KMP (optional)
    implementation("app.futured.arkitekt:decompose:LatestVersion")
    implementation("app.futured.arkitekt:decompose-annotation:LatestVersion")
    ksp("app.futured.arkitekt:decompose-processor:LatestVersion")

    // Testing
    testImplementation("app.futured.arkitekt:core-test:LatestVersion")
    testImplementation("app.futured.arkitekt:cr-usecases-test:LatestVersion")
}
```

## Features

- **Compose-ready ViewModels** with built-in state management and one-shot events
- **Coroutine Use Cases** for clean, testable business logic
- **Kotlin Multiplatform** support via Decompose with Koin DI
- **KSP Factory Generator** for Decompose components
- **Result type** for safe, composable error handling
- **Testing utilities** for mocking use cases and testing ViewModels

## Documentation

Visit [arkitekt.futured.app](https://arkitekt.futured.app) for:

- [Getting Started](https://arkitekt.futured.app/getting-started/installation/)
- [Architecture Guide](https://arkitekt.futured.app/architecture/overview/)
- [Use Cases](https://arkitekt.futured.app/use-cases/overview/)
- [KMP / Decompose](https://arkitekt.futured.app/kmp/factory-generator/)
- [Testing](https://arkitekt.futured.app/testing/viewmodel-testing/)
- [Migration Guide (5.x to 6.x)](https://arkitekt.futured.app/migration/)

## License

Arkitekt is available under the MIT license. See the [LICENSE file](LICENCE) for more information.

Created with &#x2764; at Futured. Inspired by [Alfonz library](https://github.com/petrnohejl/Alfonz).
