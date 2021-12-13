
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(ProjectSettings.compileSdk)

    defaultConfig {
        applicationId = ProjectSettings.applicationId
        minSdkVersion(ProjectSettings.minSdk)
        targetSdkVersion(ProjectSettings.targetSdk)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    dataBinding {
        isEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        getByName("test").java.srcDirs("src/sharedTest/java")
        getByName("androidTest").java.srcDirs("src/sharedTest/java")
    }

    testOptions {
        unitTests.apply {
            isIncludeAndroidResources = true
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    configurations.all {
        resolutionStrategy {
            force("org.objenesis:objenesis:2.6")
        }
    }
}

dependencies {
    implementation(project(":dagger"))
    implementation(project(":rx-usecases"))
    implementation(project(":cr-usecases"))
    implementation(project(":bindingadapters"))

    implementation(kotlin(Deps.Kotlin.stdlib, KotlinCompilerVersion.VERSION))
    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))
    implementation(Deps.Kotlin.coroutines)

    implementation(Deps.AndroidX.appcompat)
    compileOnly(Deps.AndroidX.material)
    implementation(Deps.AndroidX.annnotation)
    implementation(Deps.AndroidX.vectorDrawable)
    implementation(Deps.AndroidX.multidex)

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

    // Unit tests
    testImplementation(Deps.Test.jUnit)
    testImplementation(Deps.Test.rxSchedulerRule)

    // Shared tests - local
    testImplementation(Deps.Test.testCoroutines)
    testImplementation(project(":core-test"))
    testImplementation(project(":rx-usecases-test"))
    testImplementation(project(":cr-usecases-test"))
    testImplementation(Deps.Test.mockk)
    testImplementation(Deps.Test.androidXTestRunner)
    testImplementation(Deps.Test.androidXTestCore)
    testImplementation(Deps.Test.androidXTestCoreKtx)
    testImplementation(Deps.Test.androidXCoreTesting)
    testImplementation(Deps.Test.androidXEspresso)
    testImplementation(Deps.Test.androidXJUnit)
    testImplementation(Deps.Test.androidXJUnitKtx)
    testImplementation(Deps.Test.androidXFragmentTesting)
    testImplementation(Deps.Test.robolectric)

    // Shared tests - connected
    androidTestImplementation(project(":core-test"))
    androidTestImplementation(project(":rx-usecases-test"))
    androidTestImplementation(project(":cr-usecases-test"))
    androidTestImplementation(Deps.Test.mockkAndroid)
    androidTestImplementation(Deps.Test.androidXTestRunner)
    androidTestImplementation(Deps.Test.androidXTestCore)
    androidTestImplementation(Deps.Test.androidXTestCoreKtx)
    androidTestImplementation(Deps.Test.androidXCoreTesting)
    androidTestImplementation(Deps.Test.androidXEspresso)
    androidTestImplementation(Deps.Test.androidXJUnit)
    androidTestImplementation(Deps.Test.androidXJUnitKtx)
    androidTestImplementation(Deps.Test.androidXFragmentTesting)
    androidTestImplementation(Deps.Test.robolectric)
}
