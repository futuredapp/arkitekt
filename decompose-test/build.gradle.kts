import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.kotlin.multiplatform")
    id(Deps.Plugins.mavenPublish)
}

kotlin {
    jvmToolchain(17)

    android {
        namespace = "app.futured.arkitekt.decompose.test"
        compileSdk = ProjectSettings.compileSdk
        minSdk = ProjectSettings.minSdk
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                api(project(":decompose"))
                api(Deps.Decompose.core)
                api(Deps.Decompose.essentyLifecycle)
                api(kotlin("test"))
                api(Deps.Test.testCoroutines)
            }
        }

        androidMain {
            dependencies {
                api(kotlin("test-junit"))
            }
        }
    }
}

mavenPublishing {
    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Empty(),
            sourcesJar = true,
            androidVariantsToPublish = listOf("debug", "release"),
        ),
    )
    coordinates(
        groupId = ProjectSettings.group,
        artifactId = "decompose-test",
    )
    pom {
        name = "Arkitekt Decompose Test"
        description = "Test utilities for Arkitekt Decompose module"
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
