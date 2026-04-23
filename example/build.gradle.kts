
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = ProjectSettings.compileSdk

    defaultConfig {
        applicationId = ProjectSettings.applicationId
        minSdk = ProjectSettings.minSdk
        targetSdk = ProjectSettings.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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

    namespace = "app.futured.arkitekt.sample"

    configurations.all {
        resolutionStrategy {
            force("org.objenesis:objenesis:2.6")
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)

        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

dependencies {
    implementation(project(":cr-usecases"))
    implementation(project(":compose"))

    implementation(platform(Deps.Compose.bom))

    implementation(Deps.Kotlin.coroutines)

    implementation(Deps.AndroidX.appcompat)
    compileOnly(Deps.AndroidX.material)
    implementation(Deps.Compose.activity)
    implementation(Deps.Compose.ui)
    implementation(Deps.Compose.foundation)
    implementation(Deps.Compose.material3)
    implementation(Deps.Compose.runtime)
    implementation(Deps.Compose.runtimeLivedata)
    implementation(Deps.Compose.navigation3Runtime)
    implementation(Deps.Compose.navigation3Ui)
    implementation(Deps.Compose.lifecycleViewmodelNavigation3)
    implementation(Deps.Compose.lifecycleRuntimeCompose)
    implementation(Deps.Serialization.core)
    implementation(Deps.DI.hiltViewModelCompose)

    implementation(Deps.DI.hilt)
    ksp(Deps.DI.hiltCompiler)

    // Unit tests
    testImplementation(Deps.Test.jUnit)

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

    // Shared tests - connected
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
    androidTestImplementation(Deps.Test.robolectric)
}
