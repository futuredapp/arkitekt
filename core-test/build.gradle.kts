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

    namespace = "app.futured.arkitekt.core.test"
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
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    configureBasedOnAppliedPlugins()
    coordinates(groupId = ProjectSettings.group, artifactId = "core-test")
    pom {
        arkitektPomBase()
        name = "Arkitekt Core Test"
        description = "Test utilities for Arkitekt core module"
        inceptionYear = "2018"
    }
}

dependencies {
    api(project(":core"))
    api(project(":cr-usecases"))

    implementation(Deps.Test.androidXCoreTesting)

    implementation(Deps.Test.testCoroutines)

    // Test
    testImplementation(Deps.Test.androidXTestRunner)
    testImplementation(Deps.Test.androidXTestCore)
    testImplementation(Deps.Test.jUnit)
}
