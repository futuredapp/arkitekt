plugins {
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.kotlin.multiplatform")
    id(Deps.Plugins.mavenPublish)
}

kotlin {
    jvmToolchain(17)

    android {
        namespace = "app.futured.arkitekt.decompose.test"
        compileSdk = ProjectSettings.compileSdk
        minSdk = ProjectSettings.minSdk
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                api(project(":decompose"))
                api(Deps.Decompose.core)
                api(Deps.Decompose.essentyLifecycle)
                api(kotlin("test"))
                api(Deps.Test.testCoroutines)
            }
        }

        androidMain {
            dependencies {
                api(kotlin("test-junit"))
            }
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    configureBasedOnAppliedPlugins()
    coordinates(groupId = ProjectSettings.group, artifactId = "decompose-test")
    pom {
        arkitektPomBase()
        name = "Arkitekt Decompose Test"
        description = "Test utilities for Arkitekt Decompose module"
        inceptionYear = "2026"
    }
}
