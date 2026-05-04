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

        classpath(Deps.Plugins.dokka)
    }
}

plugins {
    idea
    id(Deps.Plugins.detekt) version Versions.detekt
    id(Deps.Plugins.ktlint) version Versions.ktlint
    id(Deps.Plugins.composeCompiler) version Versions.kotlin apply false
    id(Deps.Plugins.mavenPublish) version Versions.mavenPublish apply false
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

    group = ProjectSettings.group
    version = findProperty("VERSION_NAME") as String? ?: ProjectSettings.version

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

    plugins.withId("signing") {
        extensions.findByType<SigningExtension>()?.apply {
            val hasKey = project.hasProperty("SIGNING_PRIVATE_KEY")
            val hasPassword = project.hasProperty("SIGNING_PASSWORD")
            if (hasKey && hasPassword) {
                useInMemoryPgpKeys(
                    project.properties["SIGNING_PRIVATE_KEY"].toString(),
                    project.properties["SIGNING_PASSWORD"].toString(),
                )
            }
        }
    }
}

detekt {
    autoCorrect = false
    version = Versions.detekt
    source.setFrom(
        subprojects.flatMap { sub ->
            listOf(
                "src/main/java",
                "src/commonMain/kotlin",
                "src/androidMain/kotlin",
                "src/jvmMain/kotlin",
                "src/iosMain/kotlin",
            ).map { sub.projectDir.resolve(it) }.filter { it.exists() }
        },
    )
    config.setFrom(files("detekt.yml"))
}
