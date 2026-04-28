plugins {
    id("com.android.library")
    id(Deps.Plugins.mavenPublish)
}

android {
    compileSdk = ProjectSettings.compileSdk

    defaultConfig {
        minSdk = ProjectSettings.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildFeatures {
        buildConfig = true
    }

    namespace = "app.futured.arkitekt.crusecases.test"
    testOptions {
        targetSdk = ProjectSettings.targetSdk
    }
    lint {
        targetSdk = ProjectSettings.targetSdk
        warning += setOf("InvalidPackage")
    }
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    configureBasedOnAppliedPlugins()
    coordinates(groupId = ProjectSettings.group, artifactId = "cr-usecases-test")
    pom {
        arkitektPomBase()
        name = "Arkitekt CR UseCases Test"
        description = "Test utilities for Arkitekt cr-usecases module"
        inceptionYear = "2018"
    }
}

dependencies {
    api(project(":cr-usecases"))

    implementation(Deps.Test.mockk)

    implementation(Deps.Kotlin.coroutines)

    // Test
    testImplementation(Deps.Test.testCoroutines)
    testImplementation(Deps.Test.jUnit)
}
