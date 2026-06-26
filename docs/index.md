---
hide:
  - navigation
---

# Arkitekt

An Android and Kotlin Multiplatform architecture library built on Jetpack Compose and Kotlin Coroutines.

Arkitekt is a testable foundation for Android and KMP apps. It offers two architecture paths that share the same core principles:

- **Android / Compose**: ViewModel + Hilt + Jetpack Compose
- **[Decompose](https://github.com/arkivanov/Decompose) / KMP**: BaseComponent + Koin + Kotlin Multiplatform

---

## Key features

- Compose-ready ViewModels with built-in state management and one-shot events
- Coroutine use cases for testable business logic (`UseCase` and `FlowUseCase`)
- Kotlin Multiplatform support via Decompose with Koin dependency injection
- A KSP factory generator for Decompose components (`@GenerateFactory`)
- `kotlin.Result` integration with `getOrCancel` for safe coroutine error handling
- Testing utilities for mocking use cases, testing ViewModels, and testing Decompose components

---

## Quick installation

```kotlin
dependencies {
    // Android / Compose
    implementation("app.futured.arkitekt:core:{{ arkitekt_version }}")
    implementation("app.futured.arkitekt:cr-usecases:{{ arkitekt_version }}")
    implementation("app.futured.arkitekt:compose:{{ arkitekt_version }}")

    // Decompose / KMP (optional)
    implementation("app.futured.arkitekt:decompose:{{ arkitekt_version }}")
    implementation("app.futured.arkitekt:decompose-annotation:{{ arkitekt_version }}")
    ksp("app.futured.arkitekt:decompose-processor:{{ arkitekt_version }}")
}
```

See [Installation](getting-started/installation.md) for full details and snapshot builds.

---

## Get started

### Android / Compose

Set up a Jetpack Compose project with ViewModel, ViewState, and Hilt.

[Project Setup](getting-started/project-setup-android.md){ .md-button } [Quick Start](getting-started/quick-start-android.md){ .md-button }

### Decompose / KMP

Set up a Kotlin Multiplatform project with BaseComponent, Koin, and Decompose.

[Project Setup](getting-started/project-setup-kmp.md){ .md-button } [Quick Start](getting-started/quick-start-kmp.md){ .md-button }

---

## Sample projects

The sample projects below use the same patterns as this documentation.

### Android / Compose

The [`example`](https://github.com/futuredapp/arkitekt/tree/5.x/example) module in this repository contains a minimal Android sample. For a complete project setup, see the [Android Project Template](https://github.com/futuredapp/android-project-template-compose).

### Decompose / KMP

The [KMP Futured Template](https://github.com/futuredapp/kmp-futured-template) is a full Kotlin Multiplatform project (Android + iOS) using BaseComponent, Koin, Decompose navigation, and KSP factory generation.

