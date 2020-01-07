rootProject.name = "mvvm-android"
rootProject.buildFileName = "build.gradle.kts"

include(
    ":mvvm",
    ":dagger",
    ":example",
    ":example-minimal",
    ":rx-usecases",
    ":cr-usecases",
    ":bindingadapters",
    ":templates",
    ":mvvm-lint",
    ":mvvm-test",
    ":rx-usecases-test",
    ":cr-usecases-test"
)

pluginManagement {
    repositories {
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
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
