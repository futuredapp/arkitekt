rootProject.name = "mvvm-android"
rootProject.buildFileName = "build.gradle.kts"

include(
    ":mvvm",
    ":dagger",
    ":example",
    ":example-minimal",
    ":interactors",
    ":bindingadapters",
    ":cr-interactors",
    ":templates"
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
                useModule("${ProjectSettings.Templates.group}:${ProjectSettings.Templates.module}:${requested.version}")
            }
        }
    }
}
