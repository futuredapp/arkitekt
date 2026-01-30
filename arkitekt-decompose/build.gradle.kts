plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.vanniktech.maven.publish")
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
