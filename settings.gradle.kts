rootProject.name = "arkitekt"
rootProject.buildFileName = "build.gradle.kts"

include(
    ":core",
    ":arkitekt-decompose",
    ":arkitekt-decompose:annotation",
    ":arkitekt-decompose:processor",
    ":dagger",
    ":example",
    ":example-minimal",
    ":example-hilt",
    ":cr-usecases",
    ":arkitekt-lint",
    ":core-test",
    ":cr-usecases-test"
)
