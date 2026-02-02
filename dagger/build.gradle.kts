plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.vanniktech.maven.publish")
}

android {
    compileSdk = ProjectSettings.compileSdk

    defaultConfig {
        minSdk = ProjectSettings.minSdk
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        targetSdk = ProjectSettings.targetSdk
    }
    lint {
        targetSdk = ProjectSettings.targetSdk
    }
    namespace = "app.futured.arkitekt.dagger"
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    api(project(":core"))

    implementation(kotlin(Deps.Kotlin.reflect, Versions.kotlin))

    implementation(Deps.AndroidX.appcompat)
    compileOnly(Deps.AndroidX.material)
    implementation(Deps.AndroidX.annnotation)

    implementation(Deps.DI.hilt)
}
