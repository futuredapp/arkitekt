plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.vanniktech.maven.publish")
}

kotlin {
    jvmToolchain(17)

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
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
                implementation(platform(Deps.Compose.bom))
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
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
