rootProject.name = "arkitekt"
rootProject.buildFileName = "build.gradle.kts"

include(
    ":core",
    ":compose",
    ":arkitekt-decompose",
    ":arkitekt-decompose:annotation",
    ":arkitekt-decompose:processor",
    ":example",
    ":cr-usecases",
    ":arkitekt-lint",
    ":core-test",
    ":cr-usecases-test"
)
