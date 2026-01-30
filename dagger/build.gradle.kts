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
    testOptions {
        targetSdk = ProjectSettings.targetSdk
    }
    lint {
        targetSdk = ProjectSettings.targetSdk
    }
    namespace = "app.futured.arkitekt.dagger"
}

dependencies {
    api(project(":core"))

    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))

    implementation(Deps.AndroidX.appcompat)
    compileOnly(Deps.AndroidX.material)
    implementation(Deps.AndroidX.annnotation)

    implementation(Deps.DI.daggerSupport)
}
