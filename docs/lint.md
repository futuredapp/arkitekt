# Lint rules

Arkitekt includes custom lint checks in the `arkitekt-lint` module to enforce naming conventions for event classes.

## Rules

### MvvmEventNameMissingSuffix

**Severity:** Warning

Event class names should end with the `Event` suffix. A quickfix is provided to rename the class.

```kotlin
// Warning: missing Event suffix
class ShowDetail : Event<ViewState>()

// Correct
class ShowDetailEvent : Event<ViewState>()
```

### MvvmEventNameMisspell

**Severity:** Warning

Detects misspelled event names where extra characters appear after `Event`. A quickfix is provided to correct the name.

```kotlin
// Warning: extra characters after Event
class ShowDetailEvents : Event<ViewState>()

// Correct
class ShowDetailEvent : Event<ViewState>()
```

## Scope

These lint rules apply to all classes extending `Event<T>` from the Arkitekt `core` module. The lint checks are included automatically when you depend on the `core` module, so no extra configuration is needed.
