import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(ProjectSettings.compileSdk)

    defaultConfig {
        applicationId = ProjectSettings.applicationId + ".hilt"
        minSdkVersion(ProjectSettings.minSdk)
        targetSdkVersion(ProjectSettings.targetSdk)
        multiDexEnabled = true
        testInstrumentationRunner = "app.futured.arkitekt.examplehilt.tools.CustomTestRunner"
    }

    buildFeatures {
        dataBinding = true
    }

    hilt {
        enableTransformForLocalTests = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    sourceSets {
        getByName("test").java.srcDirs("src/sharedTest/java")
        getByName("androidTest").java.srcDirs("src/sharedTest/java")
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":cr-usecases"))
    implementation(project(":bindingadapters"))

    implementation(kotlin(Deps.Kotlin.stdlib, KotlinCompilerVersion.VERSION))
    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))
    implementation(Deps.Kotlin.coroutines)

    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.annnotation)
    implementation(Deps.AndroidX.multidex)
    implementation(Deps.AndroidX.core)
    implementation(Deps.AndroidX.fragment)
    implementation(Deps.AndroidX.navigationFragment)
    implementation(Deps.AndroidX.navigationUi)
    implementation(Deps.AndroidX.constraintLayout)

    implementation(Deps.Rx.rxKotlin)
    implementation(Deps.Rx.rxAndroid)
    implementation(Deps.Rx.rxJava)
    implementation(Deps.Rx.rxRelay)
    implementation(Deps.Rx.rxDebug)

    implementation(Deps.AndroidX.lifecycleExtensions)
    implementation(Deps.AndroidX.liveDataExtensions)
    kapt(Deps.AndroidX.lifecycleCompiler)

    implementation(Deps.DI.hilt)
    kapt(Deps.DI.hiltCompiler)
    implementation(Deps.DI.hiltViewModel)
    implementation(Deps.DI.hiltNavigationFrag)
    kapt(Deps.DI.hiltJetpackCompiler)

    // Unit tests
    testImplementation(Deps.Test.jUnit)
    testImplementation(Deps.Test.rxSchedulerRule)

    // Shared tests - local
    testImplementation(Deps.Test.testCoroutines)
    testImplementation(project(":core-test"))
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

    // Shared tests - connected (instrumented)
    androidTestImplementation(project(":core-test"))
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
    //androidTestImplementation(Deps.Test.robolectric)

    androidTestImplementation(Deps.DI.hiltTesting)
    kaptAndroidTest(Deps.DI.hiltCompiler)
    kaptAndroidTest(Deps.DI.hiltJetpackCompiler)
}
