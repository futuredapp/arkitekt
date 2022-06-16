import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = ProjectSettings.compileSdk

    defaultConfig {
        applicationId = ProjectSettings.applicationId + ".hilt"
        minSdk = ProjectSettings.minSdk
        targetSdk = ProjectSettings.targetSdk
        multiDexEnabled = true
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
    implementation(project(":cr-usecases"))
    implementation(project(":bindingadapters"))

    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))
    implementation(Deps.Kotlin.coroutines)

    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.annnotation)
    implementation(Deps.AndroidX.multidex)
    implementation(Deps.AndroidX.fragment)
    implementation(Deps.AndroidX.navigationFragment)
    implementation(Deps.AndroidX.navigationUi)
    implementation(Deps.AndroidX.constraintLayout)

    implementation(Deps.Rx.rxKotlin)
    implementation(Deps.Rx.rxAndroid)
    implementation(Deps.Rx.rxJava)
    implementation(Deps.Rx.rxRelay)
    implementation(Deps.Rx.rxDebug)

    implementation(Deps.AndroidX.liveDataExtensions)
    kapt(Deps.AndroidX.lifecycleCompiler)

    implementation(Deps.DI.hilt)
    kapt(Deps.DI.hiltCompiler)
    implementation(Deps.DI.hiltNavigationFrag)
    implementation(Deps.DI.hiltNavigation)
    kapt(Deps.DI.hiltJetpackCompiler)
}
