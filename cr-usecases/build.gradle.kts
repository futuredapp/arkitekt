import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id(Deps.Plugins.mavenPublish)
}

kotlin {
    jvmToolchain(17)

    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Deps.Kotlin.coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(Deps.Test.testCoroutines)
                implementation(Deps.Test.jUnitApi)
            }
        }
    }
}

mavenPublishing {
    coordinates(
        groupId = ProjectSettings.group,
        artifactId = "cr-usecases",
        version = project.findProperty("VERSION_NAME") as String? ?: "6.X.X-SNAPSHOT",
    )
    configure(
        KotlinMultiplatform(
            javadocJar = JavadocJar.Empty(),
            sourcesJar = true,
            androidVariantsToPublish = listOf("debug", "release"),
        ),
    )
    pom {
        name = "Arkitekt CR UseCases"
        description = "Coroutine based use cases for Arkitekt framework"
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

android {
    namespace = "app.futured.arkitekt.crusecases"
    compileSdk = ProjectSettings.compileSdk
    defaultConfig {
        minSdk = ProjectSettings.minSdk
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
