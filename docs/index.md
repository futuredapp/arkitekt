---
hide:
  - navigation
---

# Arkitekt

!!! warning
    These docs are work-in-progress for upcoming Arkitekt release

**A modern Android & Kotlin Multiplatform architecture library built on Jetpack Compose and Kotlin Coroutines.**

Arkitekt gives you a solid, testable foundation for building Android and KMP applications. It provides two architecture paths with the same core principles:

- **Android / Compose** — ViewModel + Hilt + Jetpack Compose
- **Decompose / KMP** — BaseComponent + Koin + Kotlin Multiplatform

---

## Key Features

- **Compose-ready ViewModels** with built-in state management and one-shot events
- **Coroutine Use Cases** for clean, testable business logic (`UseCase` and `FlowUseCase`)
- **Kotlin Multiplatform** support via Decompose with Koin dependency injection
- **KSP Factory Generator** for Decompose components (`@GenerateFactory`)
- **Result type** for safe, composable error handling in coroutine chains
- **Testing utilities** for mocking use cases and testing ViewModels

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

