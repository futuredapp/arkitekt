import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.apollographql.apollo")
    id("com.squareup.sqldelight")
}
group = "com.rudolfhladik.arkitektexample"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}
sqldelight {
    database("CommonDatabase") {
        packageName = "com.rudolfhladik.arkitektexample.db"
    }
}
kotlin {
    cocoapods {
        // Configure fields required by CocoaPods.
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"

        // You can change the name of the produced framework.
        // By default, it is the name of the Gradle project.
        frameworkName = "shared"
    }
    ios()
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Coroutines
                implementation(Dependencies.KotlinX.Coroutines.coroutinesCore) {
                    version { strictly(Dependencies.KotlinX.Coroutines.version) }
                }
                // Serialization
                implementation(Dependencies.KotlinX.Serialization.serialization)

                // Ktor
                implementation(Dependencies.Ktor.ktor)
                // Apollo
                implementation(Dependencies.Apollo.apollo)
                // SQL Delight
                implementation(Dependencies.SQLDelight.runtime)
                implementation(Dependencies.SQLDelight.coroutineExtensions)

                // Arkitekt usecases
                api("app.futured.arkitekt:km-usecases:0.1.4-SNAPSHOT")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                // Ktor android engine
                implementation(Dependencies.Ktor.androidEngine)
                // SQLDelight android driver
                implementation(Dependencies.SQLDelight.androidDriver)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.12")
            }
        }
        val iosMain by getting {
            dependencies {
                // Ktor iOS engine
                implementation(Dependencies.Ktor.iOsEngine)
                // SQLDelight iOS driver
                implementation(Dependencies.SQLDelight.nativeDriver)
            }
        }
        val iosTest by getting
    }
}
android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}
// apollo needs this
tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)
