---
hide:
  - navigation
---

# Arkitekt

**A modern Android & Kotlin Multiplatform architecture library built on Jetpack Compose and Kotlin Coroutines.**

Arkitekt gives you a solid, testable foundation for building Android and KMP applications. It provides two architecture paths with the same core principles:

- **Android / Compose** — ViewModel + Hilt + Jetpack Compose
- **[Decompose](https://github.com/arkivanov/Decompose) / KMP** — BaseComponent + Koin + Kotlin Multiplatform

---

## Key Features

- **Compose-ready ViewModels** with built-in state management and one-shot events
- **Coroutine Use Cases** for clean, testable business logic (`UseCase` and `FlowUseCase`)
- **Kotlin Multiplatform** support via Decompose with Koin dependency injection
- **KSP Factory Generator** for Decompose components (`@GenerateFactory`)
- **`kotlin.Result` integration** with `getOrCancel` for safe coroutine error handling
- **Testing utilities** for mocking use cases, testing ViewModels, and testing Decompose components

---

## Quick Installation

```kotlin
dependencies {
    // Android / Compose
    implementation("app.futured.arkitekt:core:{{ arkitekt.version }}")
    implementation("app.futured.arkitekt:cr-usecases:{{ arkitekt.version }}")
    implementation("app.futured.arkitekt:compose:{{ arkitekt.version }}")

    // Decompose / KMP (optional)
    implementation("app.futured.arkitekt:decompose:{{ arkitekt.version }}")
    implementation("app.futured.arkitekt:decompose-annotation:{{ arkitekt.version }}")
    ksp("app.futured.arkitekt:decompose-processor:{{ arkitekt.version }}")
}
```

See [Installation](getting-started/installation.md) for full details and snapshot builds.

---

## Get Started

### Android / Compose

Set up a Jetpack Compose project with ViewModel, ViewState, and Hilt.

[Project Setup](getting-started/project-setup-android.md){ .md-button } [Quick Start](getting-started/quick-start-android.md){ .md-button }

### Decompose / KMP

Set up a Kotlin Multiplatform project with BaseComponent, Koin, and Decompose.

[Project Setup](getting-started/project-setup-kmp.md){ .md-button } [Quick Start](getting-started/quick-start-kmp.md){ .md-button }

---

## Sample Projects

See Arkitekt in action in working applications that follow the same architectural patterns described in this documentation.

### Android / Compose

The [`example`](https://github.com/futuredapp/arkitekt/tree/5.x/example) module in this repository contains a minimal Android sample. For a full real-world project setup, see the [Android Project Template](https://github.com/futuredapp/android-project-template-compose).

### Decompose / KMP

A full Kotlin Multiplatform project (Android + iOS) using BaseComponent, Koin, Decompose navigation, and KSP factory generation — [KMP Futured Template](https://github.com/futuredapp/kmp-futured-template).

