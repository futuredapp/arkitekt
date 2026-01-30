import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
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
    namespace = "app.futured.arkitekt.sample"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":dagger"))

    implementation(platform(Deps.Compose.bom))

    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))

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
