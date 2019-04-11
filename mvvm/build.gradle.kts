import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.github.dcendents.android-maven")
}

android {
    compileSdkVersion(ProjectSettings.compileSdk)

    defaultConfig {
        minSdkVersion(ProjectSettings.minSdk)
        targetSdkVersion(ProjectSettings.targetSdk)
    }

    dataBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(kotlin(Deps.Kotlin.stdlib, KotlinCompilerVersion.VERSION))
    implementation(kotlin(Deps.Kotlin.reflect, KotlinCompilerVersion.VERSION))
    implementation(Deps.javaX)

    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.annnotation)
    implementation(Deps.AndroidX.lifecycleExtensions)
    implementation(Deps.AndroidX.livedataExtensions)
    kapt(Deps.AndroidX.lifecycleCompiler)

    testImplementation(Deps.Test.jUnit)
    testImplementation(Deps.Test.assertJ)
    testImplementation(Deps.Test.mockitoKotlin)
    testImplementation(Deps.AndroidX.archTesting)
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
