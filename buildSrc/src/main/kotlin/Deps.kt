object Deps {
    const val gradlePlugin = "com.android.tools.build:gradle:${Versions.gradle}"
    const val javaX = "javax.inject:javax.inject:${Versions.javaX}"

    object Plugins {
        const val detekt = "io.gitlab.arturbosch.detekt"
        const val ktlint = "org.jlleitschuh.gradle.ktlint"
        const val androidMaven = "com.github.dcendents:android-maven-gradle-plugin:${Versions.androidMaven}"
    }

    object Kotlin {
        const val gradlePlugin = "gradle-plugin"
        const val stdlib = "stdlib-jdk7"
        const val reflect = "reflect"
    }

    object DI {
        const val daggerSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
        const val daggerProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:${Versions.androidx}"
        const val annnotation = "androidx.annotation:annotation:${Versions.androidx}"
        const val palette = "androidx.palette:palette:${Versions.androidx}"
        const val ktx = "androidx.core:core-ktx:${Versions.androidx}"
        const val vectordrawable = "androidx.vectordrawable:vectordrawable:${Versions.androidx}"

        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.arch_components}"
        const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.arch_components}"
        const val databindingCompiler = "androidx.databinding:databinding-compiler:${Versions.databinding}"

        const val archTesting = "androidx.arch.core:core-testing:${Versions.arch_components}"
    }

    object Test {
        const val jUnit = "junit:junit:${Versions.jUnit}"
        const val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"
        const val mockitoKotlin = "com.nhaarman:mockito-kotlin-kt1.1:${Versions.mockitoKotlin}"
    }
}
