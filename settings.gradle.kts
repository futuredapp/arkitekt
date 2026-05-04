rootProject.name = "arkitekt"
rootProject.buildFileName = "build.gradle.kts"

include(
    ":core",
    ":compose",
    ":decompose",
    ":decompose-annotation",
    ":decompose-processor",
    ":decompose-test",
    ":example",
    ":cr-usecases",
    ":arkitekt-lint",
    ":core-test",
    ":cr-usecases-test",
)
