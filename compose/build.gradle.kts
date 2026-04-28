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
    }

    lint {
        targetSdk = ProjectSettings.targetSdk
    }

    namespace = "app.futured.arkitekt.compose"
}

kotlin {
    jvmToolchain(17)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    configureBasedOnAppliedPlugins()
    coordinates(groupId = ProjectSettings.group, artifactId = "compose")
    pom {
        arkitektPomBase()
        name = "Arkitekt Compose"
        description = "Compose UI module of Arkitekt framework"
        inceptionYear = "2018"
    }
}

dependencies {
    api(project(":core"))
    api(project(":cr-usecases"))

    implementation(Deps.AndroidX.viewModelExtensions)

    implementation(platform(Deps.Compose.bom))
    implementation(Deps.Compose.runtime)
}
