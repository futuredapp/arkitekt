plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = ProjectSettings.compileSdk

    defaultConfig {
        applicationId = ProjectSettings.applicationId + ".minimal"
        minSdk = ProjectSettings.minSdk
        targetSdk = ProjectSettings.targetSdk
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    namespace = "app.futured.arkitekt.sample"
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":dagger"))

    implementation(platform(Deps.Compose.bom))

    implementation(kotlin(Deps.Kotlin.reflect, Versions.kotlin))

    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.annnotation)
    implementation(Deps.Compose.activity)
    implementation(Deps.Compose.ui)
    implementation(Deps.Compose.foundation)
    implementation(Deps.Compose.material3)
    implementation(Deps.Compose.runtime)
    implementation(Deps.Compose.runtimeLivedata)
    implementation(Deps.Compose.navigation)

    implementation(Deps.DI.dagger)
    ksp(Deps.DI.daggerCompiler)
}
