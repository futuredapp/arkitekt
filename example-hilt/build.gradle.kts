plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = ProjectSettings.compileSdk

    defaultConfig {
        applicationId = ProjectSettings.applicationId + ".hilt"
        minSdk = ProjectSettings.minSdk
        targetSdk = ProjectSettings.targetSdk
        multiDexEnabled = true
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    namespace = "app.futured.arkitekt.sample.hilt"
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":cr-usecases"))

    implementation(platform(Deps.Compose.bom))

    implementation(kotlin(Deps.Kotlin.reflect, Versions.kotlin))
    implementation(Deps.Kotlin.coroutines)

    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.annnotation)
    implementation(Deps.AndroidX.multidex)
    implementation(Deps.AndroidX.fragment)

    implementation(Deps.AndroidX.liveDataExtensions)
    implementation(Deps.Compose.activity)
    implementation(Deps.Compose.ui)
    implementation(Deps.Compose.foundation)
    implementation(Deps.Compose.material3)
    implementation(Deps.Compose.runtime)
    implementation(Deps.Compose.runtimeLivedata)
    implementation(Deps.Compose.navigation)
    implementation(Deps.DI.hilt)
    ksp(Deps.DI.hiltCompiler)
    implementation(Deps.DI.hiltNavigationCompose)
    ksp(Deps.DI.hiltJetpackCompiler)
}
