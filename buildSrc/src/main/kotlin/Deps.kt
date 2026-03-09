object Deps {
    const val gradlePlugin = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
    const val javaX = "javax.inject:javax.inject:${Versions.javaX}"

    object Plugins {
        const val detekt = "io.gitlab.arturbosch.detekt"
        const val ktlint = "org.jlleitschuh.gradle.ktlint"
        const val mavenPublish = "com.vanniktech.maven.publish"
        const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.dokka}"

        const val composeCompiler = "org.jetbrains.kotlin.plugin.compose"

    }

    object Kotlin {
        const val gradlePlugin = "gradle-plugin"
        const val stdlib = "stdlib-jdk7"

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    }

    object DI {
        const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
        const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"

        const val hiltViewModelCompose = "androidx.hilt:hilt-lifecycle-viewmodel-compose:${Versions.hiltJetpack}"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"

        const val multidex = "androidx.multidex:multidex:${Versions.multidex}"
        const val material = "com.google.android.material:material:${Versions.material}"

        const val liveDataExtensions = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifeCycle}"
        const val viewModelExtensions = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifeCycle}"
        const val archTesting = "androidx.arch.core:core-testing:${Versions.test}"
    }

    object Compose {
        const val bom = "androidx.compose:compose-bom:${Versions.composeBom}"
        const val runtime = "androidx.compose.runtime:runtime"
        const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata"
        const val ui = "androidx.compose.ui:ui"
        const val foundation = "androidx.compose.foundation:foundation"
        const val material3 = "androidx.compose.material3:material3"
        const val navigation3Runtime = "androidx.navigation3:navigation3-runtime:${Versions.navigation3}"
        const val navigation3Ui = "androidx.navigation3:navigation3-ui:${Versions.navigation3}"
        const val lifecycleViewmodelNavigation3 = "androidx.lifecycle:lifecycle-viewmodel-navigation3:${Versions.lifecycleViewmodelNav3}"
        const val lifecycleRuntimeCompose = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.lifeCycle}"
        const val activity = "androidx.activity:activity-compose:${Versions.activityCompose}"
        const val jetbrainsRuntime = "org.jetbrains.compose.runtime:runtime:${Versions.jetbrainsComposeRuntime}"
    }

    object Decompose {
        const val core = "com.arkivanov.decompose:decompose:${Versions.decompose}"
        const val essentyLifecycle = "com.arkivanov.essenty:lifecycle:${Versions.essenty}"
    }

    object Ksp {
        const val api = "com.google.devtools.ksp:symbol-processing-api:${Versions.ksp}"
    }

    object Poet {
        const val interop = "com.squareup:kotlinpoet-ksp:${Versions.poet}"
    }

    object Serialization {
        const val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.serialization}"
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
        const val androidXTestRunner = "androidx.test:runner:${Versions.androidxTestRunner}"
        const val androidXTestCore = "androidx.test:core:${Versions.androidxTestCore}"
        const val androidXTestCoreKtx = "androidx.test:core-ktx:${Versions.androidxTestCore}"
        const val androidXCoreTesting = "androidx.arch.core:core-testing:${Versions.androidxTestCoreTesting}"
        const val androidXEspresso = "androidx.test.espresso:espresso-core:${Versions.androidXEspresso}"
        const val androidXJUnit = "androidx.test.ext:junit:${Versions.androidXJUnit}"
        const val androidXJUnitKtx = "androidx.test.ext:junit-ktx:${Versions.androidXJUnit}"
        const val androidXFragmentTesting = "androidx.fragment:fragment-testing:${Versions.androidXFragmentTesting}"
        const val jUnit = "androidx.test.ext:junit:${Versions.jUnit}"
        const val jUnitApi = "junit:junit:4.13.2"
        const val testCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    }
}
