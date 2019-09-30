object ProjectSettings {
    const val applicationId = "com.thefuntasty.mvvmsample"
    const val compileSdk = 28
    const val targetSdk = 28
    const val minSdk = 18

    object Templates {
        /**
         * this version will be used only for local builds, jitpack will automatically provide TAG version
         */
        const val version = "0.0.1-test"
        const val group = "com.github.thefuntasty"
        const val module = "mvvm-android"
        const val id = "$group.$module"
        const val name = "copyTemplates"
        const val implementationClass = "com.thefuntasty.mvvm.templates.Templates"
    }
}
