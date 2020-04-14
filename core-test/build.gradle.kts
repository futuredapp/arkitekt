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

    lintOptions {
        warning("InvalidPackage")
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":dagger"))
    implementation(project(":cr-usecases"))

    implementation(Deps.Test.mockk)

    implementation(Deps.Test.rxSchedulerRule)
    implementation(Deps.Test.androidXCoreTesting)

    implementation(Deps.DI.daggerSupport)

    implementation(kotlin(Deps.Kotlin.stdlib, KotlinCompilerVersion.VERSION))
    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))

    implementation(Deps.AndroidX.lifecycleExtensions)
    implementation(Deps.AndroidX.liveDataExtensions)
    implementation(Deps.Test.testCoroutines)

    // RxJava
    implementation(Deps.Rx.rxKotlin)
    implementation(Deps.Rx.rxAndroid)
    implementation(Deps.Rx.rxJava)

    // Test
    testImplementation(Deps.Test.androidXTestRunner)
    testImplementation(Deps.Test.androidXTestCore)
    testImplementation(Deps.Test.jUnit)
}

project.apply {
    extensions.add("artifact", ProjectSettings.CoreTest.artifact)
    extensions.add("libraryName", ProjectSettings.CoreTest.artifact)
    extensions.add("libraryDescription", ProjectSettings.CoreTest.libraryDescription)
}

apply("../publish.script.gradle")
