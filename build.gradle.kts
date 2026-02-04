import app.futured.arkitekt.DependencyUpdates
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.net.URI

buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath(Deps.gradlePlugin)
        classpath(kotlin(Deps.Kotlin.gradlePlugin, Versions.kotlin))
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}")
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${Versions.ksp}")
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
    id(Deps.Plugins.composeCompiler) version Versions.kotlin apply false
}

tasks {
    register<DependencyUpdates>("dependencyUpdates")
}

allprojects {
    repositories {
        google()
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
    source = files(
        "example/src/main/java",
        "core/src/main/java",
        "compose/src/main/java",
        "core-test/src/main/java",
        "cr-usecases/src/main/java",
        "cr-usecases-test/src/main/java",
        "arkitekt-decompose/src/commonMain/kotlin",
        "arkitekt-decompose/src/androidMain/kotlin",
        "arkitekt-decompose/annotation/src/commonMain/kotlin",
        "arkitekt-decompose/processor/src/jvmMain/kotlin",
        "arkitekt-lint/src/main/java"
    )
//    filters = ".*/resources/.*,.*/build/.*"
    config = files("detekt.yml")
}
