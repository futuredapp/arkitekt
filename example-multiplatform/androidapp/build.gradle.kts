plugins {
    id("com.android.application")
    kotlin("android")
}

repositories {
    mavenLocal()
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "app.futured.arkitekt.kmmsample.androidApp"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.appcompat:appcompat:1.2.0")
//    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation ("androidx.activity:activity-ktx:1.2.0-beta01")
}
