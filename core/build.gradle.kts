plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
    id(Deps.Plugins.mavenPublish)
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

    namespace = "app.futured.arkitekt.core"
}

kotlin {
    jvmToolchain(17)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    configureBasedOnAppliedPlugins()
    coordinates(groupId = ProjectSettings.group, artifactId = "core")
    pom {
        arkitektPomBase()
        name = "Arkitekt Core"
        description = "Core module of Arkitekt framework"
        inceptionYear = "2018"
    }
}

dependencies {
    implementation(platform(Deps.Compose.bom))
    implementation(Deps.Compose.runtime)

    implementation(Deps.javaX)

    api(Deps.AndroidX.liveDataExtensions)
    api(Deps.AndroidX.viewModelExtensions)
    implementation(Deps.Kotlin.coroutines)
    implementation(Deps.Kotlin.coroutinesAndroid)

    lintPublish(project(":arkitekt-lint"))
}
