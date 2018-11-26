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
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    }

    object Rx {
        const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
        const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
        const val rxRelay = "com.jakewharton.rxrelay2:rxrelay:${Versions.rxRelay}"
        const val rxDebug = "com.sumera.rxdebug:rxdebug:${Versions.rxDebug}"
        const val rxIdler = "com.squareup.rx.idler:rx2-idler:${Versions.rxIdler}"
    }

    object Test {
        const val jUnit = "junit:junit:${Versions.jUnit}"
        const val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"
        const val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.mockitoKotlin}"
    }
}
