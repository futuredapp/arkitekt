object Deps {
    const val gradlePlugin = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
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
        const val annnotation = "androidx.annotation:annotation:${Versions.androidxAnnotation}"
        const val material = "com.google.android.material:material:${Versions.material}"
        const val vectorDrawable = "androidx.vectordrawable:vectordrawable:${Versions.vectorDrawable}"

        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifeCycle}"
        const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifeCycle}"
        const val liveDataExtensions = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifeCycle}"
        const val viewModelExtensions = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifeCycle}"

        const val archTesting = "androidx.arch.core:core-testing:${Versions.test}"
        const val fragment = "androidx.fragment:fragment:${Versions.androidxFragment}"
    }

    object Rx {
        const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
        const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
        const val rxRelay = "com.jakewharton.rxrelay2:rxrelay:${Versions.rxRelay}"
        const val rxDebug = "com.sumera.rxdebug:rxdebug:${Versions.rxDebug}"
    }

    object Lint {
        const val core = "com.android.tools.lint:lint:${Versions.androidTools}"
        const val api = "com.android.tools.lint:lint-api:${Versions.androidTools}"
        const val checks = "com.android.tools.lint:lint-checks:${Versions.androidTools}"
        const val tests = "com.android.tools.lint:lint-tests:${Versions.androidTools}"
    }

    object Test {
        const val mockk = "io.mockk:mockk:${Versions.mockk}"
        const val mockkAndroid = "io.mockk:mockk-android:${Versions.mockk}"
        const val mockitoCore = "org.mockito:mockito-core:${Versions.mockitoCore}"
        const val androidXTestRunner = "androidx.test:runner:${Versions.androidxTestRunner}"
        const val androidXTestCore = "androidx.test:core:${Versions.androidxTestCore}"
        const val androidXTestCoreKtx = "androidx.test:core-ktx:${Versions.androidxTestCoreKtx}"
        const val androidXCoreTesting = "androidx.arch.core:core-testing:${Versions.androidxTestCoreTesting}"
        const val androidXEspresso = "androidx.test.espresso:espresso-core:${Versions.androidXEspresso}"
        const val androidXJUnit = "androidx.test.ext:junit:${Versions.androidXJUnit}"
        const val androidXJUnitKtx = "androidx.test.ext:junit-ktx:${Versions.androidXJUnitKtx}"
        const val androidXFragmentTesting = "androidx.fragment:fragment-testing:${Versions.androidXFragmentTesting}"
        const val rxSchedulerRule = "com.github.Plastix.RxSchedulerRule:rx2:${Versions.rxSchedulerRule}"
        const val jUnit = "androidx.test.ext:junit:${Versions.jUnit}"
        const val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"
        const val mockitoKotlin = "com.nhaarman:mockito-kotlin-kt1.1:${Versions.mockitoKotlin}"
        const val testCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    }
}
