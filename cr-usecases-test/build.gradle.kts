plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.vanniktech.maven.publish")
}

android {
    compileSdk = ProjectSettings.compileSdk

    defaultConfig {
        minSdk = ProjectSettings.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    namespace = "app.futured.arkitekt.crusecases.test"
    testOptions {
        targetSdk = ProjectSettings.targetSdk
    }
    lint {
        targetSdk = ProjectSettings.targetSdk
        warning += setOf("InvalidPackage")
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    api(project(":cr-usecases"))

    implementation(Deps.Test.mockk)

    implementation(kotlin(Deps.Kotlin.reflect, Versions.kotlin))
    implementation(Deps.Kotlin.coroutines)

    // Test
    testImplementation(Deps.Test.testCoroutines)
    testImplementation(Deps.Test.jUnit)
}
