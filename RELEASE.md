## Arkitekt Release process

This document describes release process of the Arkitekt library into `MavenCentral` repository.
There are two types of publications:

- Snapshot
- Release

Following environment variables must be specified on CI machine to publish artifacts correctly:

- `ORG_GRADLE_PROJECT_mavenCentralRepositoryUsername=maven central nexus username`
- `ORG_GRADLE_PROJECT_mavenCentralRepositoryPassword=maven central nexus password`

### Snapshot

Snapshots are published automatically when a PR is merged into one of main branches (`5.x`, `6.x`
etc.)
Snapshots version names are aggregated by major version, for example `5.X.X-SNAPSHOT`. CI run
specification: [.github/workflows/publish_snapshot.yml](.github/workflows/publish_snapshot.yml).

### Release

Release is GPG signed publication of library. Release can be created by defining the new release
within GitHub UI. Make sure you name release correctly and use a correct main branch. For example,
version `5.4.1` must originate from branch `5.x`.

Make sure you update property `VERSION_NAME` in [gradle.properties](gradle.properties) and target
branch in CI
pipeline ([.github/workflows/publish_snapshot.yml](.github/workflows/publish_snapshot.yml))
definition within new major version release.

Following additional environment variables must be defined on CI machine to sign artifact properly:

- `ORG_GRADLE_PROJECT_SIGNING_PRIVATE_KEY=GPG signing key`
- `ORG_GRADLE_PROJECT_SIGNING_PASSWORD=GPG password`

CI run specification: [.github/workflows/publish_release.yml](.github/workflows/publish_release.yml)
.
