import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(ProjectSettings.compileSdk)

    defaultConfig {
        applicationId = ProjectSettings.applicationId + ".minimal"
        minSdkVersion(ProjectSettings.minSdk)
        targetSdkVersion(ProjectSettings.targetSdk)
    }

    dataBinding {
        isEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":mvvm"))
    implementation(project(":dagger"))
    implementation(project(":rx-usecases"))

    implementation(kotlin(Deps.Kotlin.stdlib, KotlinCompilerVersion.VERSION))
    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))

    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.annnotation)

    implementation(Deps.Rx.rxKotlin)
    implementation(Deps.Rx.rxAndroid)
    implementation(Deps.Rx.rxJava)
    implementation(Deps.Rx.rxRelay)
    implementation(Deps.Rx.rxDebug)

    implementation(Deps.AndroidX.lifecycleExtensions)
    kapt(Deps.AndroidX.lifecycleCompiler)

    implementation(Deps.DI.daggerSupport)
    kapt(Deps.DI.daggerProcessor)
    kapt(Deps.DI.daggerCompiler)
}
