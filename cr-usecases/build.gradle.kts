plugins {
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.kotlin.multiplatform")
    id(Deps.Plugins.mavenPublish)
}

kotlin {
    jvmToolchain(17)

    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    android {
        namespace = "app.futured.arkitekt.crusecases"
        compileSdk = ProjectSettings.compileSdk
        minSdk = ProjectSettings.minSdk
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(Deps.Kotlin.coroutines)
            }
        }
        commonTest {
            dependencies {
                implementation(Deps.Test.testCoroutines)
                implementation(Deps.Test.jUnitApi)
            }
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    configureBasedOnAppliedPlugins()
    coordinates(groupId = ProjectSettings.group, artifactId = "cr-usecases")
    pom {
        arkitektPomBase()
        name = "Arkitekt CR UseCases"
        description = "Coroutine based use cases for Arkitekt framework"
        inceptionYear = "2018"
    }
}
