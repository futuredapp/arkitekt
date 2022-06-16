import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = ProjectSettings.compileSdk

    defaultConfig {
        applicationId = ProjectSettings.applicationId + ".minimal"
        minSdk = ProjectSettings.minSdk
        targetSdk = ProjectSettings.targetSdk
    }

    dataBinding {
        isEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-Xcontext-receivers"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":dagger"))
    implementation(project(":rx-usecases"))

    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))

    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.annnotation)

    implementation(Deps.Rx.rxKotlin)
    implementation(Deps.Rx.rxAndroid)
    implementation(Deps.Rx.rxJava)
    implementation(Deps.Rx.rxRelay)
    implementation(Deps.Rx.rxDebug)

    kapt(Deps.AndroidX.lifecycleCompiler)

    implementation(Deps.DI.daggerSupport)
    kapt(Deps.DI.daggerProcessor)
    kapt(Deps.DI.daggerCompiler)
}
