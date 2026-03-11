# Maven Publishing Migration — Arkitekt 6.x

## Context

Arkitekt 6.x migrates Maven publishing configuration to use the current `com.vanniktech.maven.publish` plugin (0.34.0) with proper `mavenPublishing {}` DSL blocks. Reference implementation is the `donut` project.

**Problem:** Publishing plugin is outdated (0.21.0), modules apply the plugin but have no `mavenPublishing {}` blocks configured, and the KMP `decompose` module has no publishing config at all.

## Scope

Only Maven publishing is being updated. `buildSrc/Deps.kt` structure, `libs.versions.toml`, and convention plugins are **out of scope**.

---

## Changes

### 1. Plugin Version — `buildSrc/src/main/kotlin/Versions.kt`

```kotlin
const val mavenPublish = "0.34.0"  // was 0.21.0
```

### 2. gradle.properties

Add:
```properties
mavenCentralPublishing=true
signAllPublications=true
```

### 3. mavenPublishing blocks — all 6 modules

Each module gets a `mavenPublishing {}` block. Version is always sourced from the `VERSION_NAME` Gradle property:

```kotlin
version = project.findProperty("VERSION_NAME") as String? ?: "6.X.X-SNAPSHOT"
```

#### `core/build.gradle.kts`
```kotlin
mavenPublishing {
    coordinates(
        groupId = ProjectSettings.group,
        artifactId = "core",
        version = project.findProperty("VERSION_NAME") as String? ?: "6.X.X-SNAPSHOT"
    )
    pom {
        name = "Arkitekt Core"
        description = "Core module of Arkitekt framework"
        url = "https://github.com/futuredapp/arkitekt"
        licenses {
            license {
                name = "MIT"
                url = "https://github.com/futuredapp/arkitekt/blob/master/LICENCE"
            }
        }
        scm {
            connection = "scm:git:git://github.com/futuredapp/arkitekt.git"
            developerConnection = "scm:git:ssh://github.com/futuredapp/arkitekt.git"
            url = "https://github.com/futuredapp/arkitekt"
        }
        developers {
            developer {
                id = "futured"
                name = "Futured"
                url = "https://futured.app"
            }
        }
    }
}
```

Remaining modules follow the same POM template, varying only `artifactId` and `name`:

| Module | artifactId | name |
|--------|-----------|------|
| `core` | `core` | `Arkitekt Core` |
| `compose` | `compose` | `Arkitekt Compose` |
| `cr-usecases` | `cr-usecases` | `Arkitekt CR UseCases` |
| `core-test` | `core-test` | `Arkitekt Core Test` |
| `cr-usecases-test` | `cr-usecases-test` | `Arkitekt CR UseCases Test` |
| `decompose` | `decompose` | `Arkitekt Decompose` |

#### KMP-specific: `decompose/build.gradle.kts`

The `decompose` module additionally needs Android variant publishing in the `kotlin {}` block:

```kotlin
androidTarget {
    publishLibraryVariants("release", "debug")
    // ...
}
```

---

## CI Publishing

Snapshots and releases are published using the `VERSION_NAME` Gradle property:

```shell
# Snapshot (per commit to main)
./gradlew publish -PVERSION_NAME=6.X.X-SNAPSHOT

# Release (after GitHub release is published)
./gradlew publish -PVERSION_NAME=${{ github.event.release.name }}
```

Signing keys are passed via `SIGNING_PRIVATE_KEY` and `SIGNING_PASSWORD` properties (already wired in root `build.gradle.kts`).

---

## Verification

After implementation:
1. `./gradlew :core:publishToMavenLocal` — verify local publish works
2. `./gradlew :decompose:publishToMavenLocal` — verify KMP publish works
3. Check `~/.m2/repository/app/futured/arkitekt/` for published artifacts

---

## Open Questions

- Should `annotation` and `processor` submodules of `decompose` also be published? - YES it should
- Should `arkitekt-lint` be published? - NO
