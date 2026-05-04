plugins {
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id(Deps.Plugins.mavenPublish)
}

kotlin {
    jvmToolchain(17)

    android {
        namespace = "app.futured.arkitekt.decompose.android"
        compileSdk = ProjectSettings.compileSdk
        minSdk = ProjectSettings.minSdk
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain  {
            dependencies {
                implementation(Deps.Decompose.core)
                implementation(Deps.Decompose.essentyLifecycle)
                implementation(Deps.Kotlin.coroutines)
                implementation(Deps.Compose.jetbrainsRuntime)
                implementation(Deps.Serialization.core)
            }
        }

        commonTest {
            dependencies {
                implementation(project(":decompose-test"))
            }
        }

        androidMain {
            dependencies {
                implementation(project.dependencies.platform(Deps.Compose.bom))
                implementation(Deps.Compose.runtime)
            }
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    configureBasedOnAppliedPlugins()
    coordinates(groupId = ProjectSettings.group, artifactId = "decompose")
    pom {
        arkitektPomBase()
        name = "Arkitekt Decompose"
        description = "KMP Decompose integration for Arkitekt framework"
        inceptionYear = "2026"
    }
}
