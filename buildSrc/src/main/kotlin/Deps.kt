object Deps {
    const val gradlePlugin = "com.android.tools.build:gradle:${Versions.gradle}"
    const val javaX = "javax.inject:javax.inject:${Versions.javaX}"

    object Plugins {
        const val detekt = "io.gitlab.arturbosch.detekt"
        const val ktlint = "org.jlleitschuh.gradle.ktlint"
        const val androidMaven = "com.github.dcendents:android-maven-gradle-plugin:${Versions.androidMaven}"
        const val bintray = "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.bintray}"
        const val dokka = "org.jetbrains.dokka:dokka-android-gradle-plugin:${Versions.dokka}"
    }

    object Kotlin {
        const val gradlePlugin = "gradle-plugin"
        const val stdlib = "stdlib-jdk7"
        const val reflect = "reflect"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
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
        const val material = "com.google.android.material:material:${Versions.androidx}"
        const val ktx = "androidx.core:core-ktx:${Versions.androidxCore}"
        const val vectordrawable = "androidx.vectordrawable:vectordrawable:${Versions.vectorDrawable}"

        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.archComponents}"
        const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.archComponents}"
        const val livedataExtensions = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.archComponents}"
        const val viewmodelExtensions = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.archComponents}"

        const val archTesting = "androidx.arch.core:core-testing:${Versions.test}"
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
        const val mockitoCore = "org.mockito:mockito-core:${Versions.mockitoCore}"
        const val androidXTestRunnner = "androidx.test:runner:${Versions.androidxTestRunner}"
        const val androidXTestCore = "androidx.test:core:${Versions.androidxTestCore}"
        const val rxSchedulerRule = "com.github.Plastix.RxSchedulerRule:rx2:${Versions.rxSchedulerRule}"
        const val jUnit = "androidx.test.ext:junit:${Versions.jUnit}"
        const val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"
        const val mockitoKotlin = "com.nhaarman:mockito-kotlin-kt1.1:${Versions.mockitoKotlin}"
        const val testCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    }
}
