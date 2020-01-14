object ProjectSettings {
    const val applicationId = "com.thefuntasty.mvvmsample"
    const val compileSdk = 28
    const val targetSdk = 28
    const val minSdk = 18
    const val group = "com.thefuntasty.mvvm"

    /**
     * this version will be used only for local builds, jitpack will automatically provide TAG version
     */
    val version = System.getenv("BITRISE_GIT_TAG")?:"0.0.1-TEST"

    object Templates {
        const val module = "android-templates"
        const val id = "$group.$module"
        const val name = "copyTemplates"
        const val implementationClass = "com.thefuntasty.mvvm.templates.Templates"
    }
    object Mvvm {
        const val artifact = "mvvm"
        const val libraryDescription = "Core module of MVVM-Android framework"
    }

    object MvvmTest {
        const val artifact = "mvvm-test"
        const val libraryDescription = "Test utilities for mvvm module"
    }

    object RxUseCases {
        const val artifact = "rx-usecases"
        const val libraryDescription = "RxJava based use cases meant to be used with MVVM-Android framework"
    }

    object RxUseCasesTest {
        const val artifact = "rx-usecases-test"
        const val libraryDescription = "Test utilities for rx-usecases module"
    }

    object CrUseCases {
        const val artifact = "cr-usecases"
        const val libraryDescription = "Coroutine based use cases meant to be used with MVVM-Android framework"
    }

    object Dagger {
        const val artifact = "dagger"
        const val libraryDescription = "Dagger ready base classes meant to be used with MVVM-Android framework"
    }

    object  BindingAdapters {
        const val artifact = "bindingadapters"
        const val libraryDescription = "Collection of handy extensions and binding adapters usable even without rest of MVVM-Android framework"
    }

    object Publish {
        const val bintrayRepo = "mvvm-android"
        const val siteUrl = "https://github.com/thefuntasty/mvvm-android"
        const val gitUrl = "https://github.com/thefuntasty/mvvm-android.git"
        const val developerId = "TheFuntasty"
        const val developerName = "TheFuntasty"
        const val developerEmail = "ops@thefuntasty.com"
        const val licenseName = "MIT Licence"
        const val licenseUrl = "https://github.com/thefuntasty/mvvm-android/blob/master/LICENCE"
        val allLicenses = listOf("MIT")
    }
}
