plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.vanniktech.maven.publish")
}

android {
    compileSdk = ProjectSettings.compileSdk

    defaultConfig {
        minSdk = ProjectSettings.minSdk
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        targetSdk = ProjectSettings.targetSdk
        unitTests.apply {
            isIncludeAndroidResources = true
        }
    }

    lint {
        targetSdk = ProjectSettings.targetSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    namespace = "app.futured.arkitekt.core"
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(platform(Deps.Compose.bom))

    implementation(kotlin(Deps.Kotlin.reflect, Versions.kotlin))
    implementation(Deps.javaX)

    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.material)
    implementation(Deps.AndroidX.annnotation)
    api(Deps.AndroidX.liveDataExtensions)
    api(Deps.AndroidX.viewModelExtensions)
    implementation(Deps.AndroidX.fragment)
    implementation(Deps.Compose.runtime)
    implementation(Deps.Kotlin.coroutines)
    implementation(Deps.Kotlin.coroutinesAndroid)

    testImplementation(Deps.Test.jUnit)
    testImplementation(Deps.Test.assertJ)
    testImplementation(Deps.Test.mockitoKotlin)
    testImplementation(Deps.AndroidX.archTesting)
    testImplementation(Deps.Test.robolectric)
    testImplementation(Deps.Test.androidXTestCore)

    lintPublish(project(":arkitekt-lint"))
}
