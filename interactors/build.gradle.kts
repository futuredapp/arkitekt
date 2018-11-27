import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.github.dcendents.android-maven")
}

android {
    compileSdkVersion(ProjectSettings.compileSdk)

    defaultConfig {
        minSdkVersion(ProjectSettings.minSdk)
        targetSdkVersion(ProjectSettings.targetSdk)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    compileOptions {
        setSourceCompatibility(JavaVersion.VERSION_1_8)
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }
}

dependencies {
    implementation(project(":mvvm"))

    implementation(kotlin(Deps.Kotlin.stdlib, KotlinCompilerVersion.VERSION))
    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))

    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.annnotation)
    implementation(Deps.AndroidX.lifecycleExtensions)
    kapt(Deps.AndroidX.lifecycleCompiler)

    implementation(Deps.DI.daggerSupport)

    // RxJava
    implementation(Deps.Rx.rxKotlin)
    implementation(Deps.Rx.rxAndroid)
    implementation(Deps.Rx.rxJava)
    implementation(Deps.Rx.rxRelay)
    implementation(Deps.Rx.rxDebug)

    testImplementation("org.mockito:mockito-core:2.8.9")
    testImplementation("androidx.test:runner:1.1.0")
    testImplementation(Deps.Test.mockitoKotlin)
    testImplementation("junit:junit:4.12")
    testImplementation("androidx.test:core:1.0.0")
    testImplementation("com.github.Plastix.RxSchedulerRule:rx2:1.0.2")

}

tasks {
    val sourcesJar by creating(type = Jar::class) {
        from(android.sourceSets.getByName("main").java.srcDirs)
        classifier = "sources"
    }

    artifacts {
        add("archives", sourcesJar)
    }
}
