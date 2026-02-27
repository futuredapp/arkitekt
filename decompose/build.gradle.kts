import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id(Deps.Plugins.mavenPublish)
}

kotlin {
    jvmToolchain(17)

    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Decompose.core)
                implementation(Deps.Decompose.essentyLifecycle)
                implementation(Deps.Koin.core)
                implementation(Deps.Kotlin.coroutines)
                implementation(Deps.Logging.kermit)
                implementation(Deps.Compose.jetbrainsRuntime)
                implementation(Deps.Serialization.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(Deps.Test.testCoroutines)
            }
        }

        val androidMain by getting {
            dependencies {
                api(project(":cr-usecases"))
                implementation(Deps.Compose.runtime)
            }
        }
    }
}

mavenPublishing {
    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Empty(),
            sourcesJar = true,
            androidVariantsToPublish = listOf("debug", "release")
        )
    )
    coordinates(
        groupId = ProjectSettings.group,
        artifactId = "decompose",
        version = project.findProperty("VERSION_NAME") as String? ?: "6.X.X-SNAPSHOT"
    )
    pom {
        name = "Arkitekt Decompose"
        description = "KMP Decompose integration for Arkitekt framework"
        url = "https://github.com/futuredapp/arkitekt"
        licenses {
            license {
                name = "MIT"
                url = "https://github.com/futuredapp/arkitekt/blob/master/LICENCE"
            }
        }
        scm {
            connection = "scm:git:git://github.com/futuredapp/arkitekt.git"
            developerConnection = "scm:git:ssh://github.com/futuredapp/arkitekt.git"
            url = "https://github.com/futuredapp/arkitekt"
        }
        developers {
            developer {
                id = "futured"
                name = "Futured"
                url = "https://futured.app"
            }
        }
    }
}

android {
    namespace = "app.futured.arkitekt.decompose.android"
    compileSdk = ProjectSettings.compileSdk
    defaultConfig {
        minSdk = ProjectSettings.minSdk
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
