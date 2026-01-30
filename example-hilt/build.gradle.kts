import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
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
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "app.futured.arkitekt.sample.hilt"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":cr-usecases"))

    implementation(platform(Deps.Compose.bom))

    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))
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
