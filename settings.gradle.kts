rootProject.name = "arkitekt"
rootProject.buildFileName = "build.gradle.kts"

include(
    ":core",
    ":dagger",
    ":example",
    ":example-minimal",
    ":example-hilt",
    ":rx-usecases",
    ":cr-usecases",
    ":bindingadapters",
    ":arkitekt-lint",
    ":core-test",
    ":rx-usecases-test",
    ":cr-usecases-test"
)
