rootProject.name = "arkitekt"
rootProject.buildFileName = "build.gradle.kts"

include(
    ":core",
    ":dagger",
    ":example",
    ":example-minimal",
    ":rx-usecases",
    ":cr-usecases",
    ":bindingadapters",
    ":templates",
    ":arkitekt-lint",
    ":core-test",
    ":rx-usecases-test",
    ":cr-usecases-test",
    ":km-usecases"
)

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        flatDir {
            dirs("templates/build/libs")
        }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "app.futured.arkitekt.templates") {
                useModule("app.futured.arkitekt:templates:${requested.version}")
            }
        }
    }
}
