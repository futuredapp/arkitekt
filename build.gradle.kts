import app.futured.arkitekt.DependencyUpdates
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.net.URI

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(Deps.gradlePlugin)
        classpath(kotlin(Deps.Kotlin.gradlePlugin, Versions.kotlin))
        classpath(Deps.DI.hiltPlugin)
        classpath(Deps.AndroidX.safeArgsPlugin)
        classpath(Deps.Plugins.mavenPublish)
        classpath(Deps.Plugins.dokka)
    }
}

plugins {
    idea
    id(Deps.Plugins.detekt) version Versions.detekt
    id(Deps.Plugins.ktlint) version Versions.ktlint
}

tasks {
    register<DependencyUpdates>("dependencyUpdates")
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
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
        }
    }

    plugins.whenPluginAdded {
        if (this is SigningPlugin) {
            extensions.findByType<SigningExtension>()?.apply {
                val hasKey = project.hasProperty("SIGNING_PRIVATE_KEY")
                val hasPassword = project.hasProperty("SIGNING_PASSWORD")
                if (hasKey && hasPassword) {
                    useInMemoryPgpKeys(
                        project.properties["SIGNING_PRIVATE_KEY"].toString(),
                        project.properties["SIGNING_PASSWORD"].toString()
                    )
                }
            }
        }
    }
}

detekt {
    autoCorrect = false
    version = Versions.detekt
    input = files(
        "example/src/main/java",
        "core/src/main/java",
        "core-test/src/main/java",
        "dagger/src/main/java",
        "rx-usecases/src/main/java",
        "rx-usecases-test/src/main/java",
        "cr-usecases/src/main/java",
        "cr-usecases-test/src/main/java",
        "bindingadapters/src/main/java",
        "arkitekt-lint/src/main/java"
    )
//    filters = ".*/resources/.*,.*/build/.*"
    config = files("detekt.yml")
}
