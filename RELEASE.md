## Arkitekt Release process

This document describes release process of the Arkitekt library into `MavenCentral` repository.
There are two types of publications:

- Snapshot
- Release

Both publications are GPG signed, so the following environment variables must be specified on the CI
machine for either publication to succeed:

- `ORG_GRADLE_PROJECT_mavenCentralUsername=maven central username`
- `ORG_GRADLE_PROJECT_mavenCentralPassword=maven central password`
- `ORG_GRADLE_PROJECT_SIGNING_PRIVATE_KEY=GPG signing key`
- `ORG_GRADLE_PROJECT_SIGNING_PASSWORD=GPG password`

On CI these are wired from the `MAVEN_CENTRAL_REPOSITORY_USERNAME`, `MAVEN_CENTRAL_REPOSITORY_PASSWORD`,
`GPG_SIGNING_PRIVATE_KEY` and `GPG_SIGNING_PASSWORD` secrets.

### Snapshot

Snapshots are published automatically when a commit lands on the current main branch (`6.x`).
Snapshot version names are aggregated by major version and passed on the command line, for example
`6.X.X-SNAPSHOT`. CI run
specification: [.github/workflows/publish_snapshot.yml](.github/workflows/publish_snapshot.yml).

### Release

A release is created by publishing a new release within the GitHub UI. The release version is taken
from the **release name** (`github.event.release.name`) and passed to Gradle as
`-PVERSION_NAME=<release name>`, so make sure you name the release correctly (for example `6.0.0`)
and target the matching main branch — for example version `6.0.0` must originate from branch `6.x`.

The version is not stored in [gradle.properties](gradle.properties). When `VERSION_NAME` is not
supplied, the build falls back to the default in
[buildSrc/src/main/kotlin/ProjectSettings.kt](buildSrc/src/main/kotlin/ProjectSettings.kt).

When starting a new major version, update the branch triggers in the snapshot and pages CI pipelines
([.github/workflows/publish_snapshot.yml](.github/workflows/publish_snapshot.yml),
[.github/workflows/publish_pages.yml](.github/workflows/publish_pages.yml)) and the snapshot version
name within [.github/workflows/publish_snapshot.yml](.github/workflows/publish_snapshot.yml).

After a release is published, the `update-docs-version` job automatically opens a pull request that
bumps `extra.arkitekt_version` in [mkdocs.yml](mkdocs.yml) to the released version.

CI run specification: [.github/workflows/publish_release.yml](.github/workflows/publish_release.yml).
