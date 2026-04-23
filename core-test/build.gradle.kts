import com.vanniktech.maven.publish.AndroidSingleVariantLibrary

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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

mavenPublishing {
    configure(AndroidSingleVariantLibrary(publishJavadocJar = false))
    coordinates(
        groupId = ProjectSettings.group,
        artifactId = "core-test",
    )
    pom {
        name = "Arkitekt Core Test"
        description = "Test utilities for Arkitekt core module"
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
    api(project(":core"))
    api(project(":cr-usecases"))

    implementation(Deps.Test.androidXCoreTesting)

    implementation(Deps.Test.testCoroutines)

    // Test
    testImplementation(Deps.Test.androidXTestRunner)
    testImplementation(Deps.Test.androidXTestCore)
    testImplementation(Deps.Test.jUnit)
}
