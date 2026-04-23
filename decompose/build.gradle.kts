import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id(Deps.Plugins.mavenPublish)
}

kotlin {
    jvmToolchain(17)

    android {
        namespace = "app.futured.arkitekt.decompose.android"
        compileSdk = ProjectSettings.compileSdk
        minSdk = ProjectSettings.minSdk
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain  {
            dependencies {
                implementation(Deps.Decompose.core)
                implementation(Deps.Decompose.essentyLifecycle)
                implementation(Deps.Kotlin.coroutines)
                implementation(Deps.Compose.jetbrainsRuntime)
                implementation(Deps.Serialization.core)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(Deps.Test.testCoroutines)
            }
        }

        androidMain {
            dependencies {
                implementation(project.dependencies.platform(Deps.Compose.bom))
                implementation(Deps.Compose.runtime)
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
        artifactId = "decompose",
    )
    pom {
        name = "Arkitekt Decompose"
        description = "KMP Decompose integration for Arkitekt framework"
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
