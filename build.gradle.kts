import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.net.URI

buildscript {
    repositories {
        google()
        jcenter()
        maven { setUrl("https://jitpack.io") }
        flatDir {
            dirs("templates/build/libs")
        }
    }
    dependencies {
        classpath(Deps.gradlePlugin)
        classpath(Deps.Plugins.androidMaven)
        classpath(kotlin(Deps.Kotlin.gradlePlugin, Versions.kotlin))
    }
}

plugins {
    idea
    id(Deps.Plugins.detekt) version Versions.detekt
    id(Deps.Plugins.ktlint) version Versions.ktlint

//    uncomment this line for local testing purposes during template module development
//    id(ProjectSettings.Templates.id) version ProjectSettings.Templates.version
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url = URI("https://jitpack.io") }
    }
}

subprojects {
    apply(plugin = Deps.Plugins.ktlint)

    ktlint {
        version.set(Versions.ktlintExtension)
        ignoreFailures.set(true)
        android.set(true)
        outputToConsole.set(true)
        reporters.set(setOf(ReporterType.PLAIN, ReporterType.CHECKSTYLE))
    }
}

detekt {
    version = Versions.detekt
    input = files(
        "example/src/main/java",
        "mvvm/src/main/java",
        "dagger/src/main/java",
        "bindingadapters/src/main/java",
        "interactors/src/main/java"
    )
    filters = ".*/resources/.*,.*/build/.*"
    config = files("detekt.yml")
}
