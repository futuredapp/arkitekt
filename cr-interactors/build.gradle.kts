import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.github.dcendents.android-maven")
    id("kotlin-kapt")
    id("com.jfrog.bintray")
    id("org.jetbrains.dokka-android")
}

group = ProjectSettings.group
version = ProjectSettings.version

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
    implementation(Deps.AndroidX.viewmodelExtensions)
    implementation(Deps.Kotlin.coroutines)

    testImplementation(Deps.Test.jUnit)
    testImplementation(Deps.Test.assertJ)
    testImplementation(Deps.Test.testCoroutines)
}

tasks {
    val sourcesJar by creating(type = Jar::class) {
        from(android.sourceSets.getByName("main").java.srcDirs)
        classifier = "sources"
    }

    val dokka by getting(DokkaTask::class) {
        outputFormat = "javadoc"
        outputDirectory = "$buildDir/dokka"
    }

    val kotlinDocJar by creating(type = Jar::class) {
        dependsOn(dokka)
        classifier = "javadoc"
        from("$buildDir/dokka")
    }

    artifacts {
        add("archives", sourcesJar)
        add("archives", kotlinDocJar)
    }
}

project.apply {
    extensions.add("artifact", ProjectSettings.CrInteractors.artifact)
    extensions.add("libraryName", ProjectSettings.CrInteractors.artifact)
    extensions.add("libraryDescription", ProjectSettings.CrInteractors.libraryDescription)
}

apply("../publish.script.gradle")
