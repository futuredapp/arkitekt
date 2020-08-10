import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

group = ProjectSettings.group
version = ProjectSettings.version

android {
    compileSdkVersion(ProjectSettings.compileSdk)

    defaultConfig {
        minSdkVersion(ProjectSettings.minSdk)
        targetSdkVersion(ProjectSettings.targetSdk)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":core"))

    implementation(kotlin(Deps.Kotlin.stdlib, KotlinCompilerVersion.VERSION))
    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))

    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.annnotation)
    implementation(Deps.AndroidX.lifecycleExtensions)
    kapt(Deps.AndroidX.lifecycleCompiler)

    implementation(Deps.DI.daggerSupport)

    // RxJava
    implementation(Deps.Rx.rxKotlin)
    implementation(Deps.Rx.rxAndroid)
    implementation(Deps.Rx.rxJava)
    implementation(Deps.Rx.rxRelay)
    implementation(Deps.Rx.rxDebug)

    testImplementation(Deps.Test.mockitoCore)
    testImplementation(Deps.Test.androidXTestRunner)
    testImplementation(Deps.Test.mockitoKotlin)
    testImplementation(Deps.Test.jUnit)
    testImplementation(Deps.Test.androidXTestCore)
    testImplementation(Deps.Test.rxSchedulerRule)
}

project.apply {
    extensions.add("artifact", ProjectSettings.RxUseCases.artifact)
    extensions.add("libraryName", ProjectSettings.RxUseCases.artifact)
    extensions.add("libraryDescription", ProjectSettings.RxUseCases.libraryDescription)
}

apply("../publish.script.gradle")
