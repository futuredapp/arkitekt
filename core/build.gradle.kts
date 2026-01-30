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
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
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

    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "app.futured.arkitekt.core"
}

dependencies {
    implementation(platform(Deps.Compose.bom))

    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))
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
