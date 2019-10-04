object ProjectSettings {
    const val applicationId = "com.thefuntasty.mvvmsample"
    const val compileSdk = 28
    const val targetSdk = 28
    const val minSdk = 18

    object Templates {
        /**
         * this version will be used only for local builds, jitpack will automatically provide TAG version
         */
        val version = System.getenv("BITRISE_GIT_TAG")?:"0.0.1-TEST"
        const val group = "com.thefuntasty.mvvm"
        const val module = "android-templates"
        const val id = "$group.$module"
        const val name = "copyTemplates"
        const val implementationClass = "com.thefuntasty.mvvm.templates.Templates"
    }
}
