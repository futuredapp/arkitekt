import org.jetbrains.kotlin.config.KotlinCompilerVersion

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

    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "app.futured.arkitekt.core.test"
    testOptions {
        targetSdk = ProjectSettings.targetSdk
    }

    lint {
        targetSdk = ProjectSettings.targetSdk
        warning += setOf("InvalidPackage")
    }
}

dependencies {
    api(project(":core"))
    api(project(":dagger"))
    api(project(":cr-usecases"))

    implementation(Deps.Test.mockk)

    implementation(Deps.Test.androidXCoreTesting)

    implementation(Deps.DI.daggerSupport)

    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))

    implementation(Deps.AndroidX.liveDataExtensions)
    implementation(Deps.Test.testCoroutines)

    // Test
    testImplementation(Deps.Test.androidXTestRunner)
    testImplementation(Deps.Test.androidXTestCore)
    testImplementation(Deps.Test.jUnit)
}
