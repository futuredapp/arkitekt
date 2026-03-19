import com.vanniktech.maven.publish.AndroidSingleVariantLibrary

plugins {
    id("com.android.library")
    id("kotlin-android")
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

mavenPublishing {
    configure(AndroidSingleVariantLibrary(publishJavadocJar = false))
    coordinates(
        groupId = ProjectSettings.group,
        artifactId = "core",
        version = project.findProperty("VERSION_NAME") as String? ?: "6.X.X-SNAPSHOT"
    )
    pom {
        name = "Arkitekt Core"
        description = "Core module of Arkitekt framework"
        url = "https://github.com/futuredapp/arkitekt"
        licenses {
            license {
                name = "MIT"
                url = "https://github.com/futuredapp/arkitekt/blob/master/LICENCE"
            }
        }
        scm {
            connection = "scm:git:git://github.com/futuredapp/arkitekt.git"
            developerConnection = "scm:git:ssh://github.com/futuredapp/arkitekt.git"
            url = "https://github.com/futuredapp/arkitekt"
        }
        developers {
            developer {
                id = "futured"
                name = "Futured"
                url = "https://futured.app"
            }
        }
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
