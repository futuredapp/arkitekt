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
    ":cr-usecases-test"
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
            if (requested.id.id == ProjectSettings.Templates.id) {
                useModule("${ProjectSettings.group}:${ProjectSettings.Templates.module}:${requested.version}")
            }
        }
    }
}
