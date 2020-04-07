object ProjectSettings {
    const val applicationId = "app.futured.arkitekt.sample"
    const val compileSdk = 28
    const val targetSdk = 28
    const val minSdk = 18
    const val group = "app.futured.arkitekt"

    /**
     * this version will be used only for local builds, jitpack will automatically provide TAG version
     */
    val version = System.getenv("BITRISE_GIT_TAG")?:"0.0.1-TEST"

    object Templates {
        const val module = "templates"
        const val id = "$group.$module"
        const val name = "copyTemplates"
        const val implementationClass = "app.futured.arkitekt.templates.Templates"
    }
    object Core {
        const val artifact = "core"
        const val libraryDescription = "Core module of MVVM-Android framework"
    }

    object CoreTest {
        const val artifact = "core-test"
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

    object CrUseCasesTest {
        const val artifact = "cr-usecases-test"
        const val libraryDescription = "Test utilities for cr-usecases module"
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
        const val siteUrl = "https://github.com/futuredapp/arkitekt"
        const val gitUrl = "https://github.com/futuredapp/arkitekt.git"
        const val developerId = "FuturedApp"
        const val developerName = "FuturedApp"
        const val developerEmail = "ops@futured.app"
        const val licenseName = "MIT Licence"
        const val licenseUrl = "https://github.com/futuredapp/arkitekt/blob/master/LICENCE"
        val allLicenses = listOf("MIT")
    }
}
